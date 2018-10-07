# Party Schedule Service 

Backend for Assembly schedule management and viewing

## Running

TODO

## Development

### Building

Requirements:
- JDK8
- Maven

Building the final package:

```sh
mvn package
```

Will create a `pss-*.jar` in the `target` directory.

Compile and execute without building a package (useful during development):

```sh
mvn compile exec:java
```

TODO: Docker things

### Running

Simply execute:

```sh
java -jar pss-*.jar
```

TODO: Docker things

### API/Development

Swagger UI is exposed at: http://localhost:8080/swagger-ui.html

Swagger apidoc is exposed at: http://localhost:8080/v2/api-docs
