# Party Schedule Service 

Backend for Assembly schedule management and viewing

## Running

TODO

## Development

### Building

Requirements:
- JDK8
- Maven

Building the final jar package:

```sh
mvn package
```

Will create a `pss-*.jar` in the `target` directory.

Compile and execute without building a package (useful during development):

```sh
mvn compile exec:java
```

Database:

For now, set up a local MySQL/MariaDB instance, database and user: (TODO: improve this)

```sql
create user pss@localhost identified by 'pss';
create database pss;
grant all on pss.* to pss@localhost;
```

and throw in the database with example data:

```sh
mysql -upss -ppss pss < pss.sql
```

TODO: Docker things

### Running

(After having the jar built) simply execute:

```sh
java -jar pss-*.jar
```

TODO: Docker things

### API/Development

Swagger UI is exposed at: http://localhost:8080/swagger-ui.html

Swagger apidoc is exposed at: http://localhost:8080/v2/api-docs
