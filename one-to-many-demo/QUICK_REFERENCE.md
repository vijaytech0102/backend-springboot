# One-to-Many Quick Reference Guide

## Project Summary
A Spring Boot application demonstrating **One-to-Many** relationship between Author and Book entities with complete CRUD operations.

## Quick Links
- **Main Documentation**: [README.md](README.md)
- **API Reference**: [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
- **Getting Started**: [HELP.md](HELP.md)

---

## 🏗️ Architecture

```
Author (1) ──────── (Many) Book
   ↓ @OneToMany          ↓ @ManyToOne
Repository → Service → Controller → REST API
```

---

## 🚀 Quick Start

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Test
mvn test
```

Access: `http://localhost:8080`

---

## 📌 Essential Concepts

### @OneToMany (Parent Side)
```java
@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Book> books;
```
- **mappedBy**: Refers to field in child entity
- **cascade**: Propagates operations
- **orphanRemoval**: Deletes unused children

### @ManyToOne (Child Side)
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "author_id")
private Author author;
```
- **fetch**: LAZY is default and recommended
- **@JoinColumn**: Foreign key column name

---

## 🔌 API Endpoints (Quick List)

### Author APIs
| Action | Endpoint |
|--------|----------|
| Create | `POST /api/authors` |
| Read All | `GET /api/authors` |
| Read One | `GET /api/authors/{id}` |
| Update | `PUT /api/authors/{id}` |
| Delete | `DELETE /api/authors/{id}` |
| By Country | `GET /api/authors/country/{country}` |

### Book APIs
| Action | Endpoint |
|--------|----------|
| Create | `POST /api/books/{authorId}` |
| Read All | `GET /api/books` |
| Read One | `GET /api/books/{id}` |
| Update | `PUT /api/books/{id}` |
| Delete | `DELETE /api/books/{id}` |
| By Author | `GET /api/books/author/{authorId}` |
| By Genre | `GET /api/books/genre/{genre}` |
| Search | `GET /api/books/search/{title}` |

---

## 💾 Sample Requests

### Create Author
```json
POST /api/authors
{
  "name": "J.K. Rowling",
  "email": "jk@example.com",
  "country": "United Kingdom"
}
```

### Add Book to Author
```json
POST /api/books/1
{
  "title": "Harry Potter",
  "isbn": "978-0747532699",
  "publicationDate": "1997-06-26",
  "genre": "Fantasy",
  "price": 15.99
}
```

### Get Author with Books
```
GET /api/authors/1
```

---

## 🔄 Cascade Operations

| Operation | Result |
|-----------|--------|
| Save Author | Books are saved |
| Update Author | Relationship maintained |
| Delete Author | All Books deleted |
| Remove Book | Book deleted from DB |

---

## 📁 Project Structure

```
one-to-many-demo/
├── entity/           → Author, Book
├── repository/       → AuthorRepository, BookRepository
├── service/          → AuthorService, BookService
├── controller/       → AuthorController, BookController
├── dto/              → AuthorDTO, BookDTO
└── DataInitializer   → Sample data loader
```

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=AuthorBookServiceTest

# With coverage
mvn test jacoco:report
```

---

## 🔍 Debugging

### View All Queries
1. Set in `application.properties`:
```properties
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

### H2 Console
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
```

### Application Logs
```
logging.level.com.example.onetomany=DEBUG
```

---

## 🔐 Relationship Rules

1. **One Author → Many Books** (implicit)
2. **Many Books → One Author** (explicit via @JoinColumn)
3. **Cascade Delete**: Author deletion → Book deletion
4. **Orphan Removal**: Removing from list → Deletion
5. **Lazy Loading**: Author → Books loaded on demand
6. **Unique ISBN**: Each book ISBN is unique

---

## 📊 Database Schema

### Authors Table
```sql
id (PK), name, email, country
```

### Books Table
```sql
id (PK), title, isbn (UNIQUE), 
publicationDate, genre, price, 
author_id (FK → authors.id)
```

---

## 🛠️ Common Operations

### Add Book to Author (Entity Model)
```java
Book book = new Book();
author.addBook(book);  // Bidirectional update
repository.save(author);
```

### Get Author with Books
```java
Author author = repository.findById(1);
List<Book> books = author.getBooks();
```

### Delete Everything for Author
```java
repository.deleteById(authorId);  // Cascade deletes all books
```

---

## ⚙️ Configuration Files

### application.properties
- Database: H2 in-memory
- Hibernate DDL: create-drop
- Logging levels configured
- H2 Console enabled

### pom.xml
- Spring Boot 3.1.5
- Spring Data JPA
- Hibernate
- H2 Database
- Lombok
- JUnit 5

---

## 🚨 Common Issues

| Issue | Solution |
|-------|----------|
| Lazy load exception | Use @Transactional on service |
| N+1 query problem | Use JOIN FETCH in @Query |
| Port 8080 in use | Change to port 8081 |
| Data not persisted | Check @Transactional |
| Cascade not working | Add cascade = CascadeType.ALL |

---

## 📚 Key Files

| File | Purpose |
|------|---------|
| Author.java | Parent entity with @OneToMany |
| Book.java | Child entity with @ManyToOne |
| AuthorService.java | Business logic for Author |
| BookService.java | Business logic for Book |
| AuthorController.java | /api/authors endpoints |
| BookController.java | /api/books endpoints |
| DataInitializer.java | Loads sample data |

---

## 🎯 Learning Path

1. **Understand Entities** → Read Author.java, Book.java
2. **Learn Relationships** → Study @OneToMany, @ManyToOne
3. **Explore Services** → Review service layer logic
4. **Test APIs** → Use curl or Postman
5. **Run Tests** → Execute test suite
6. **Read Documentation** → Study README.md

---

## 📞 Need Help?

1. Check [README.md](README.md) for detailed explanations
2. Check [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for endpoint details
3. Check [HELP.md](HELP.md) for setup and troubleshooting
4. Run tests to see expected behavior
5. Review logs for error messages

---

## 🎓 What You'll Learn

- ✅ One-to-Many relationship mapping
- ✅ JPA annotations (@OneToMany, @ManyToOne)
- ✅ Cascade operations
- ✅ CRUD operations with Spring Data JPA
- ✅ REST API design
- ✅ Service layer architecture
- ✅ Exception handling
- ✅ Testing with Spring Boot

---

## 📝 Version Info

- **Spring Boot**: 3.1.5
- **Java**: 17+
- **Maven**: 3.6+
- **Database**: H2 (in-memory)

---

**Last Updated**: March 2026
