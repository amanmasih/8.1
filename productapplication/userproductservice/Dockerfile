FROM openjdk
WORKDIR usr/src
ENV MONGO_DATABASE=productsdb
ENV MONGO_URL=mongodb://localhost:27017/productsdb
ADD ./target/userproductservice-0.0.1-SNAPSHOT.jar  /usr/src/userproductservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","userproductservice-0.0.1-SNAPSHOT.jar"]