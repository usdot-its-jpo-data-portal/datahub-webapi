FROM maven:3.6.3-jdk-11-slim as builder

WORKDIR /home

COPY . .

RUN mvn clean package

FROM openjdk:11.0.8-jre-slim

RUN apt-get update \
&& apt-get install curl -y \
&& apt-get clean

COPY --from=builder /home/src/main/resources/application.properties application.properties
COPY --from=builder /home/target/datahub-webapi-1.7.0.jar datahub-webapi-1.7.0.jar

ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /datahub-webapi-1.7.0.jar" ]
