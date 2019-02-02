FROM maven:alpine as build

RUN mkdir /src
WORKDIR /src
COPY . /src
RUN /usr/bin/mvn package

FROM openjdk:8-jre-alpine as production

RUN mkdir /app
WORKDIR /app

COPY --from=build /src/target/pss-*.jar /app/

CMD ["/bin/sh", "-c", "/usr/bin/java -jar ./pss-*"]
