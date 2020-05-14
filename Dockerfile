FROM maven:3.6.1-jdk-8-alpine as builder

WORKDIR /home

COPY . .

RUN mvn clean package

FROM openjdk:8u212-jre-alpine

RUN apk add curl

COPY --from=builder /home/src/main/resources/application.properties application.properties
COPY --from=builder /home/target/datahub-webapi-1.3.0.jar datahub-webapi-1.3.0.jar

ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /datahub-webapi-1.3.0.jar" ]
