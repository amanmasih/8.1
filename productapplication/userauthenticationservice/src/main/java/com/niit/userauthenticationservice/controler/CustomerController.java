package com.niit.userauthenticationservice.controler;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.niit.userauthenticationservice.exception.CustomerAlreadyExist;
import com.niit.userauthenticationservice.exception.CustomerNotFound;
import com.niit.userauthenticationservice.model.Customer;
import com.niit.userauthenticationservice.security.SecurityTokenGenerator;
import com.niit.userauthenticationservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v2/cutomerData/")
public class CustomerController {
    CustomerService customerService;
    SecurityTokenGenerator securityTokenGenerator;
    @Autowired
    public CustomerController(CustomerService customerService, SecurityTokenGenerator securityTokenGenerator) {
        this.customerService = customerService;
        this.securityTokenGenerator = securityTokenGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<?> save(@RequestBody Customer customer) throws CustomerAlreadyExist {
        return new ResponseEntity<>(customerService.saveCustomer(customer), HttpStatus.CREATED);
    }
    @GetMapping("/login")
    @HystrixCommand(fallbackMethod = "fallbackLogin")
    @HystrixProperty(name="execution.isolation.timeoutInMilliseconds",value="1500")
    public ResponseEntity<?> findByUserEmailAndPassword(@RequestBody Customer customer) throws CustomerNotFound {
        Map<String,String> map=null;
        try {
            Customer customer1= customerService.findCustomerByCustomerIdAndPassword(customer.getCustomerId(),customer.getPassword());
            if((customer1.getCustomerId()).equals(customer.getCustomerId())){
                map=securityTokenGenerator.generateToken(customer);
            }
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
        catch (CustomerNotFound ex){
            throw new CustomerNotFound();
        }
        catch (Exception ex){
            return new ResponseEntity<>("Please try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of the function

    //for circuit breaker fallbackMethod
    public ResponseEntity<?>fallbackLogin(@RequestBody Customer customer){
        String msg = "Login Unsuccessful/barred";
        return new ResponseEntity<>(msg,HttpStatus.GATEWAY_TIMEOUT);
    }
    //for actuator to run on port with /health will activate the actuator for health status ,its a DevOPs tool
}
