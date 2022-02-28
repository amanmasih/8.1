FROM openjdk
WORKDIR usr/lib
ADD ./target/eurekaservice-0.0.1-SNAPSHOT.jar  /usr/lib/eurekaservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","eurekaservice-0.0.1-SNAPSHOT.jar"]