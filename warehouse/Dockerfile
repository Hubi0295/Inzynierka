FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/warehouse-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9880
ENTRYPOINT ["java","-jar","app.jar"]
