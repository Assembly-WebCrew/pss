FROM maven:alpine as build

RUN mkdir /src
WORKDIR /src
COPY . /src
RUN /usr/bin/mvn package

FROM openjdk:8-jre-alpine

RUN mkdir /app
COPY --from=build /src/target/pss-*.jar /app/pss.jar

WORKDIR /app
CMD java -jar pss.jar
