FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/product-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9878
ENTRYPOINT ["java","-jar","app.jar"]
