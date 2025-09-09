FROM amazoncorretto:17-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY target/gateway-0.0.1-SNAPSHOT.jar app.jar
ARG JWT_S=dev
ENV JWT_SECRET=$JWT_S
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
