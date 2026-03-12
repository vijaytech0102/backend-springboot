# Getting Started with One-to-Many Demo

## Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Git (optional)

## Quick Start

### 1. Clone or Navigate to Project
```bash
cd one-to-many-demo
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Access the Application

#### API Base URL
```
http://localhost:8080/api
```

#### H2 Console (In-Memory Database)
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## Project Structure

```
one-to-many-demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/onetomany/
│   │   │   ├── entity/          # JPA Entities (Author, Book)
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   ├── service/         # Business Logic
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   └── OneToManyDemoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/                # Integration Tests
├── pom.xml
├── README.md                    # Main Documentation
└── API_DOCUMENTATION.md         # API Reference

```

## Key Files Overview

### Entities
- **Author.java** - Parent entity with @OneToMany relationship
- **Book.java** - Child entity with @ManyToOne relationship

### Repositories
- **AuthorRepository** - Custom queries for Author entity
- **BookRepository** - Custom queries for Book entity

### Services
- **AuthorService** - CRUD operations for Author
- **BookService** - CRUD operations for Book

### Controllers
- **AuthorController** - REST endpoints for /api/authors
- **BookController** - REST endpoints for /api/books

## Common Tasks

### View All Authors
```bash
curl http://localhost:8080/api/authors
```

### Create New Author
```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","country":"USA"}'
```

### Create Book for Author (ID=1)
```bash
curl -X POST http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"My Book","genre":"Fiction","price":15.99}'
```

### View Author with All Books
```bash
curl http://localhost:8080/api/authors/1
```

### Update Book
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Title","price":19.99}'
```

### Delete Book
```bash
curl -X DELETE http://localhost:8080/api/books/1
```

### Delete Author (cascade deletes all books)
```bash
curl -X DELETE http://localhost:8080/api/authors/1
```

## Sample Data

The application automatically loads sample data on startup:
- 3 Authors (J.K. Rowling, Stephen King, George R. R. Martin)
- 7 Books distributed among the authors

You can immediately test the APIs with this data.

## Running Tests

```bash
mvn test
```

## Database Configuration

The application uses H2 in-memory database. Configuration can be found in:
```
src/main/resources/application.properties
```

### Key Configuration
- **Auto DDL**: `spring.jpa.hibernate.ddl-auto=create-drop`
- **Database**: In-memory H2
- **Console Enabled**: Yes

## Logging

Check application logs for:
- SQL queries: `logging.level.org.hibernate.SQL=DEBUG`
- Query parameters: `logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE`
- Application logs: `logging.level.com.example.onetomany=DEBUG`

## Understanding Cascade Operations

### Cascade.ALL Behavior
When an Author is:
- **Saved**: Associated Books are automatically saved
- **Updated**: Associated Books can be updated
- **Deleted**: Associated Books are automatically deleted

### Orphan Removal
When a Book is removed from Author's book list:
```java
author.removeBook(book);  // Book is deleted from database
```

## Learn More

- **README.md** - Complete documentation with concepts and examples
- **API_DOCUMENTATION.md** - Detailed API reference
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate Documentation](https://hibernate.org/)

## Troubleshooting

### Application won't start
- Ensure Java 17+ is installed: `java -version`
- Check Maven: `mvn -version`
- Clear Maven cache: `mvn clean`

### Port 8080 already in use
- Change port in `application.properties`: `server.port=8081`

### Database issues
- Check H2 console: `http://localhost:8080/h2-console`
- Verify JDBC URL: `jdbc:h2:mem:testdb`

### Cascade delete not working
- Ensure `@OneToMany` has `cascade = CascadeType.ALL`

## Next Steps

1. Read the [README.md](README.md) for detailed concepts
2. Study the [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for all endpoints
3. Run the tests: `mvn test`
4. Experiment with CRUD operations using curl or Postman
5. Modify the entities and relationships to learn more

## Support

For issues or questions:
1. Check the README.md documentation
2. Review logs in the console
3. Examine the H2 database contents
4. Run tests to understand expected behavior

---

**Happy Learning! 🚀**
