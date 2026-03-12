# One-to-Many Mapping Demo - Spring Boot

This project demonstrates **One-to-Many relationship** with complete **CRUD operations** in Spring Boot using JPA/Hibernate.

## 📚 Project Overview

The project uses an **Author - Book** relationship:
- **One** Author can write **Many** Books
- **Many** Books belong to **One** Author

### Key Concepts Covered

1. **Entity Relationships** - @OneToMany and @ManyToOne annotations
2. **Cascade Operations** - CascadeType.ALL for automatic child management
3. **Orphan Removal** - Automatic deletion of child records when removed
4. **CRUD Operations** - Create, Read, Update, Delete for both entities
5. **Repository Pattern** - Custom query methods with JPA
6. **Service Layer** - Business logic separated from controllers
7. **DTO Pattern** - Data Transfer Objects for API communication

---

## 📁 Project Structure

```
one-to-many-demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/onetomany/
│   │   │   ├── entity/
│   │   │   │   ├── Author.java          # Parent entity
│   │   │   │   └── Book.java            # Child entity
│   │   │   ├── repository/
│   │   │   │   ├── AuthorRepository.java
│   │   │   │   └── BookRepository.java
│   │   │   ├── service/
│   │   │   │   ├── AuthorService.java
│   │   │   │   └── BookService.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthorController.java
│   │   │   │   └── BookController.java
│   │   │   ├── dto/
│   │   │   │   ├── AuthorDTO.java
│   │   │   │   └── BookDTO.java
│   │   │   └── OneToManyDemoApplication.java
│   │   └── resources/
│   │       └── application.properties     # Configuration
│   └── test/
│       └── java/com/example/onetomany/
└── pom.xml
```

---

## 🔗 Entity Relationship Diagram

```
┌─────────────────┐           ┌──────────────────┐
│     AUTHOR      │           │      BOOK        │
├─────────────────┤           ├──────────────────┤
│ id (PK)         │──────────▶│ id (PK)          │
│ name            │ 1....*    │ title            │
│ email           │           │ isbn             │
│ country         │           │ genre            │
│ books (List)    │           │ price            │
└─────────────────┘           │ author_id (FK)   │
                              └──────────────────┘

One Author has Many Books
Many Books have One Author
```

---

## 💾 Database Schema

### AUTHOR Table
```sql
CREATE TABLE authors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    country VARCHAR(255)
);
```

### BOOK Table
```sql
CREATE TABLE books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) UNIQUE,
    publication_date DATE,
    genre VARCHAR(255),
    price DOUBLE,
    author_id BIGINT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES authors(id)
);
```

---

## 🛠️ Entity Classes

### Author.java (Parent Entity)
```java
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String country;
    
    // One Author has Many Books
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();
    
    // Helper methods to manage the relationship
    public void addBook(Book book) { ... }
    public void removeBook(Book book) { ... }
}
```

**Key Annotations:**
- `@OneToMany` - Defines the One-to-Many relationship
- `mappedBy = "author"` - Refers to the property in Book entity
- `cascade = CascadeType.ALL` - Propagates operations (save, delete, etc.)
- `orphanRemoval = true` - Deletes books when removed from the list

### Book.java (Child Entity)
```java
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String isbn;
    private LocalDate publicationDate;
    private String genre;
    private Double price;
    
    // Many Books belong to One Author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
```

**Key Annotations:**
- `@ManyToOne` - Defines the Many-to-One relationship
- `@JoinColumn` - Specifies foreign key column name
- `fetch = FetchType.LAZY` - Load author only when accessed

---

## 🔍 Repository Layer

### AuthorRepository.java
```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
    List<Author> findByCountry(String country);
    Optional<Author> findByEmail(String email);
    
    @Query("SELECT a FROM Author a WHERE SIZE(a.books) >= :minBooks")
    List<Author> findAuthorsWithAtLeastNBooks(@Param("minBooks") int minBooks);
}
```

### BookRepository.java
```java
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByGenre(String genre);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByTitleContainingIgnoreCase(String title);
}
```

---

## 🎯 Service Layer

### AuthorService.java
Provides business logic for Author operations:
- `createAuthor(AuthorDTO)` - Create new author
- `getAuthorById(Long id)` - Retrieve author with all books
- `getAllAuthors()` - Retrieve all authors
- `updateAuthor(Long id, AuthorDTO)` - Update author details
- `deleteAuthor(Long id)` - Delete author and cascade delete books
- `getAuthorsByCountry(String)` - Filter by country

### BookService.java
Provides business logic for Book operations:
- `createBook(BookDTO, Long authorId)` - Create book for author
- `getBookById(Long id)` - Retrieve book details
- `getAllBooks()` - Retrieve all books
- `getBooksByAuthorId(Long authorId)` - Get all books by author
- `updateBook(Long id, BookDTO)` - Update book details
- `deleteBook(Long id)` - Delete specific book
- `getBooksByGenre(String)` - Filter by genre

---

## 🌐 REST API Endpoints

### Author Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/authors` | Create new author |
| GET | `/api/authors` | Get all authors |
| GET | `/api/authors/{id}` | Get author by ID (with books) |
| GET | `/api/authors/search/{name}` | Get author by name |
| GET | `/api/authors/country/{country}` | Get authors by country |
| GET | `/api/authors/books/{minBooks}` | Get authors with N+ books |
| PUT | `/api/authors/{id}` | Update author |
| DELETE | `/api/authors/{id}` | Delete author (cascades to books) |

### Book Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/books/{authorId}` | Create new book for author |
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| GET | `/api/books/author/{authorId}` | Get books by author ID |
| GET | `/api/books/genre/{genre}` | Get books by genre |
| GET | `/api/books/search/{title}` | Search books by title |
| GET | `/api/books/expensive` | Get expensive books (above avg) |
| GET | `/api/books/price-range` | Get books in price range |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |

---

## 📝 API Usage Examples

### 1. Create an Author
```bash
POST /api/authors
Content-Type: application/json

{
  "name": "J.K. Rowling",
  "email": "jk@example.com",
  "country": "United Kingdom"
}

Response: 201 Created
{
  "id": 1,
  "name": "J.K. Rowling",
  "email": "jk@example.com",
  "country": "United Kingdom",
  "books": []
}
```

### 2. Create Books for the Author
```bash
POST /api/books/1
Content-Type: application/json

{
  "title": "Harry Potter and the Philosopher's Stone",
  "isbn": "978-0747532699",
  "publicationDate": "1997-06-26",
  "genre": "Fantasy",
  "price": 15.99
}

Response: 201 Created
{
  "id": 1,
  "title": "Harry Potter and the Philosopher's Stone",
  "isbn": "978-0747532699",
  "publicationDate": "1997-06-26",
  "genre": "Fantasy",
  "price": 15.99,
  "authorId": 1,
  "authorName": "J.K. Rowling"
}
```

### 3. Get Author with All Books
```bash
GET /api/authors/1

Response: 200 OK
{
  "id": 1,
  "name": "J.K. Rowling",
  "email": "jk@example.com",
  "country": "United Kingdom",
  "books": [
    {
      "id": 1,
      "title": "Harry Potter and the Philosopher's Stone",
      "isbn": "978-0747532699",
      "publicationDate": "1997-06-26",
      "genre": "Fantasy",
      "price": 15.99
    },
    {
      "id": 2,
      "title": "Harry Potter and the Chamber of Secrets",
      "isbn": "978-0747538494",
      "publicationDate": "1998-07-02",
      "genre": "Fantasy",
      "price": 16.99
    }
  ]
}
```

### 4. Get All Books by Author
```bash
GET /api/books/author/1

Response: 200 OK
[
  {
    "id": 1,
    "title": "Harry Potter and the Philosopher's Stone",
    "genre": "Fantasy",
    "price": 15.99,
    "authorId": 1,
    "authorName": "J.K. Rowling"
  },
  {
    "id": 2,
    "title": "Harry Potter and the Chamber of Secrets",
    "genre": "Fantasy",
    "price": 16.99,
    "authorId": 1,
    "authorName": "J.K. Rowling"
  }
]
```

### 5. Update Author
```bash
PUT /api/authors/1
Content-Type: application/json

{
  "name": "Joanne Rowling",
  "email": "joanne@example.com",
  "country": "Scotland"
}

Response: 200 OK
{
  "id": 1,
  "name": "Joanne Rowling",
  "email": "joanne@example.com",
  "country": "Scotland",
  "books": [...]
}
```

### 6. Delete Author (and cascade delete all books)
```bash
DELETE /api/authors/1

Response: 200 OK
"Author deleted successfully"

Note: All books with author_id = 1 will also be deleted
```

---

## 🚀 Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build
```bash
cd one-to-many-demo
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/one-to-many-demo-1.0.0.jar
```

### Access the Application
- **Application URL**: `http://localhost:8080`
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (leave empty)

---

## 🧪 Sample Test Data

You can populate the database with sample data by calling these endpoints in order:

```bash
# 1. Create Authors
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"name":"J.K. Rowling","email":"jk@example.com","country":"United Kingdom"}'

curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"name":"Stephen King","email":"stephen@example.com","country":"United States"}'

# 2. Create Books for Author 1
curl -X POST http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Harry Potter 1","isbn":"978-1","publicationDate":"1997-06-26","genre":"Fantasy","price":15.99}'

curl -X POST http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Harry Potter 2","isbn":"978-2","publicationDate":"1998-07-02","genre":"Fantasy","price":16.99}'

# 3. Create Books for Author 2
curl -X POST http://localhost:8080/api/books/2 \
  -H "Content-Type: application/json" \
  -d '{"title":"The Shining","isbn":"978-3","publicationDate":"1977-01-28","genre":"Horror","price":12.99}'

# 4. Retrieve data
curl http://localhost:8080/api/authors
curl http://localhost:8080/api/authors/1
curl http://localhost:8080/api/books
curl http://localhost:8080/api/books/author/1
```

---

## 🔑 Important Concepts

### 1. Cascade Operations
```java
@OneToMany(cascade = CascadeType.ALL)
```
- **PERSIST**: When author is saved, books are saved
- **MERGE**: When author is merged, books are merged
- **REMOVE**: When author is deleted, books are deleted
- **REFRESH**: When author is refreshed, books are refreshed

### 2. Orphan Removal
```java
@OneToMany(orphanRemoval = true)
```
Automatically deletes a Book when it's removed from Author's books list:
```java
author.removeBook(book); // Book is deleted from database
```

### 3. Lazy vs Eager Loading
```java
// Lazy Loading (default, recommended)
@ManyToOne(fetch = FetchType.LAZY)

// Eager Loading
@ManyToOne(fetch = FetchType.EAGER)
```

### 4. Bidirectional Relationship Management
```java
// Always set both sides
public void addBook(Book book) {
    books.add(book);
    book.setAuthor(this);  // Keep relationship in sync
}
```

---

## 📊 Advanced Queries

### Custom Repository Methods
```java
// Find authors with at least N books
@Query("SELECT a FROM Author a WHERE SIZE(a.books) >= :minBooks")
List<Author> findAuthorsWithAtLeastNBooks(@Param("minBooks") int minBooks);

// Find expensive books (above average)
@Query("SELECT b FROM Book b WHERE b.price > (SELECT AVG(b2.price) FROM Book b2)")
List<Book> findExpensiveBooks();

// Fetch with eager loading to avoid N+1 queries
@Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books")
List<Author> findAllWithBooks();
```

---

## 📚 Dependencies

- **Spring Boot 3.1.5**
- **Spring Data JPA** - Repository abstraction
- **Hibernate** - JPA implementation
- **H2 Database** - In-memory database for development
- **Lombok** - Reduce boilerplate code
- **Jakarta Persistence API** - JPA annotations

---

## 🎓 Learning Outcomes

After studying this project, you will understand:

✅ How to define One-to-Many relationships using JPA annotations
✅ How to use @OneToMany and @ManyToOne correctly
✅ Cascade operations and their impact on data
✅ Orphan removal and its use cases
✅ Bidirectional relationship management
✅ CRUD operations with JPA Repository
✅ Custom query methods with @Query
✅ DTO pattern for API communication
✅ Service layer for business logic
✅ RESTful API design with Spring Boot

---

## 🐛 Common Issues & Solutions

### Issue: Lazy initialization exception
**Cause**: Accessing lazy-loaded collections outside of transaction
**Solution**: Use `@Transactional` on service methods or use eager loading

### Issue: N+1 Query Problem
**Cause**: Loading parent then querying all children
**Solution**: Use JOIN FETCH in custom queries

### Issue: Infinite loop in JSON serialization
**Cause**: Bidirectional relationship causing circular reference
**Solution**: Use `@JsonBackReference` or DTOs

### Issue: Cascade delete not working
**Cause**: Missing `cascade = CascadeType.ALL`
**Solution**: Add cascade parameter to @OneToMany

---

## 📖 References

- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate ORM Guide](https://hibernate.org/orm/documentation/)
- [Jakarta Persistence API Specification](https://jakarta.ee/specifications/persistence/)
- [Spring Boot Reference Guide](https://spring.io/projects/spring-boot)

---

## 📄 License

This project is provided as an educational resource.

---

## ❓ FAQ

**Q: Why use DTOs instead of returning entities directly?**
A: DTOs decouple API from database schema and allow selective field exposure.

**Q: Should I use LAZY or EAGER loading?**
A: Use LAZY by default to avoid loading unnecessary data. Use EAGER only when needed.

**Q: What happens if I delete an author?**
A: All associated books are deleted automatically due to `cascade = CascadeType.ALL`.

**Q: Can I have multiple OneToMany relationships?**
A: Yes, an entity can have multiple @OneToMany relationships to different entities.

---

**Happy Learning! 🚀**
