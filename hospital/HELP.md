# HELP.md

## Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
* [Spring Web](https://spring.io/projects/spring-web)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links
These additional references should also help you:

* [Configure the Spring AOT Plugin](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/index.html#spring-aot-maven)

## Database H2 Console

H2 Console is enabled in this application. You can access it at:
```
http://localhost:8080/h2-console
```

Default credentials:
- **JDBC URL**: jdbc:h2:mem:hospitaldb
- **User Name**: sa
- **Password**: (leave empty)

## Common Commands

### Build the project
```bash
mvn clean install
```

### Run the application
```bash
mvn spring-boot:run
```

### Run tests
```bash
mvn test
```

### Package as JAR
```bash
mvn clean package
```

### View dependencies
```bash
mvn dependency:tree
```
