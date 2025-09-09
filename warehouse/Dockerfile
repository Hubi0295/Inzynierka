FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/warehouse-0.0.1-SNAPSHOT.jar app.jar
RUN apk add --no-cache netcat-openbsd
ARG DB_USER=dev
ARG DB_PASS=dev
ARG DB_NAME=dev
ARG JWT_S=dev
ENV POSTGRES_USER=$DB_USER
ENV POSTGRES_PASSWORD=$DB_PASS
ENV POSTGRES_DB=$DB_NAME
ENV JWT_SECRET=$JWT_S
EXPOSE 9880
ENTRYPOINT ["java","-jar","app.jar"]
