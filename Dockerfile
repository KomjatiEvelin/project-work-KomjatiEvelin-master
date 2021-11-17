FROM maven:3-openjdk-11 as build

LABEL maintainer="Zsolt Toth"

WORKDIR /tmp
COPY pom.xml .
ADD src src
RUN mvn package spring-boot:repackage

FROM ubuntu as ubuntu
RUN apt update
RUN apt install -y curl
RUN curl https://www.dropbox.com/s/eddjqop0ldmcx6f/SalesDB.sql?dl=0 -o SalesDB.sql

FROM openjdk:11.0-jre-slim

EXPOSE 8080
WORKDIR /usr/src/myapp
COPY --from=build /tmp/target/backend.jar .
COPY --from=ubuntu /SalesDB.sql /tmp/src/main/resources/SalesDB.sql
RUN useradd -s /bin/bash spring
RUN chown -R spring .
USER spring
CMD java -jar backend.jar
