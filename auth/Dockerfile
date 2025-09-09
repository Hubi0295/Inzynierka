FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/auth-0.0.1-SNAPSHOT.jar app.jar
RUN apk add --no-cache netcat-openbsd
EXPOSE 8761
ENTRYPOINT ["java","-jar","app.jar"]
