FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/contractorService-0.0.1-SNAPSHOT.jar app.jar
RUN apk add --no-cache netcat-openbsd
EXPOSE 9877
ENTRYPOINT ["java","-jar","app.jar"]
