FROM maven:alpine as build

RUN mkdir /src
WORKDIR /src
COPY . /src
RUN /usr/bin/mvn package

FROM openjdk:8-jre-alpine as production

ENV PSS_HTTP_PORT 5000
ENV PSS_DATABASE_URL jdbc:mysql://127.0.0.1:3306/pss
ENV PSS_DATABASE_USER pss
ENV PSS_DATABASE_PASSWORD pss

RUN mkdir /app
WORKDIR /app

COPY --from=build /src/target/pss-*.jar /app/

CMD ["/bin/sh", "-c", "/usr/bin/java -jar ./pss-*"]
