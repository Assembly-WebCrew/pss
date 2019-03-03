# Party Schedule Service 

Backend for Assembly schedule management and viewing

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

For now, set up a MySQL/MariaDB database and user manually: (TODO: improve this)

```sql
create user pss@localhost identified by 'pss';
create database pss;
grant all on pss.* to pss@localhost;
```

and throw in the database table structure:

```sh
mysql -upss -ppss pss < pss.sql
```

## Running

(After having the jar built) simply execute:

```sh
java -jar pss-*.jar
```

### Requirements

* JRE8+
* MySQL Server or compatible alternative (eg. MariaDB)

### Docker

To run the application in docker you simply need to run `docker-compose up` and it will build the backend container and spool it up along with the required database instance.

### Configuration

Configuration can be set with a config file and/or environment variables.

To set the config with a config file, copy `pss.properties.example` as `pss.properties` either to the same directory as the jar or the parent directory (=project root when local dev)

To set the config with environment variables, convert the properties to uppercase, replace dots with underscores and prefix them with `PSS_`. For example, the `http.port` in the config can be set with with `PSS_HTTP_PORT` environment variable. All available config properties are in `pss.properties.example`.

### API/Development

Swagger UI is exposed at: http://localhost:8080/swagger-ui.html

Swagger apidoc is exposed at: http://localhost:8080/v2/api-docs
