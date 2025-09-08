FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/productService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9879
ENTRYPOINT ["java","-jar","app.jar"]
