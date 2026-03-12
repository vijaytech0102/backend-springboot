# One-to-Many API Documentation

This document provides complete API documentation for the One-to-Many Demo project.

## Base URL
```
http://localhost:8080/api
```

---

## 📖 Author Endpoints

### 1. Create Author
**Endpoint:** `POST /authors`

**Description:** Create a new author

**Request Body:**
```json
{
  "name": "string (required)",
  "email": "string (optional)",
  "country": "string (optional)"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "J.K. Rowling",
  "email": "jk@example.com",
  "country": "United Kingdom",
  "books": []
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "J.K. Rowling",
    "email": "jk@example.com",
    "country": "United Kingdom"
  }'
```

---

### 2. Get All Authors
**Endpoint:** `GET /authors`

**Description:** Retrieve all authors with their books

**Query Parameters:** None

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "J.K. Rowling",
    "email": "jk@example.com",
    "country": "United Kingdom",
    "books": [
      {
        "id": 1,
        "title": "Harry Potter",
        "isbn": "978-0747532699",
        "publicationDate": "1997-06-26",
        "genre": "Fantasy",
        "price": 15.99
      }
    ]
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/authors
```

---

### 3. Get Author by ID
**Endpoint:** `GET /authors/{id}`

**Description:** Retrieve a specific author with all their books

**Path Parameters:**
- `id` (Long, required) - Author ID

**Response (200 OK):**
```json
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

**Error Response (404 Not Found):**
```
Author not found with ID: 999
```

**Example:**
```bash
curl http://localhost:8080/api/authors/1
```

---

### 4. Search Author by Name
**Endpoint:** `GET /authors/search/{name}`

**Description:** Find author by exact name match

**Path Parameters:**
- `name` (String, required) - Author name

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "J.K. Rowling",
  "email": "jk@example.com",
  "country": "United Kingdom",
  "books": [...]
}
```

**Error Response (404 Not Found):**
```
Author not found with name: Unknown Author
```

**Example:**
```bash
curl "http://localhost:8080/api/authors/search/J.K. Rowling"
```

---

### 5. Get Authors by Country
**Endpoint:** `GET /authors/country/{country}`

**Description:** Retrieve all authors from a specific country

**Path Parameters:**
- `country` (String, required) - Country name

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "J.K. Rowling",
    "email": "jk@example.com",
    "country": "United Kingdom",
    "books": [...]
  }
]
```

**Example:**
```bash
curl "http://localhost:8080/api/authors/country/United Kingdom"
```

---

### 6. Get Authors with At Least N Books
**Endpoint:** `GET /authors/books/{minBooks}`

**Description:** Retrieve authors who have written at least N books

**Path Parameters:**
- `minBooks` (Integer, required) - Minimum number of books

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "J.K. Rowling",
    "email": "jk@example.com",
    "country": "United Kingdom",
    "books": [3 books listed]
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/authors/books/2
```

---

### 7. Update Author
**Endpoint:** `PUT /authors/{id}`

**Description:** Update author details

**Path Parameters:**
- `id` (Long, required) - Author ID

**Request Body:**
```json
{
  "name": "string",
  "email": "string",
  "country": "string"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Joanne Rowling",
  "email": "joanne@example.com",
  "country": "Scotland",
  "books": [...]
}
```

**Example:**
```bash
curl -X PUT http://localhost:8080/api/authors/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Joanne Rowling",
    "email": "joanne@example.com",
    "country": "Scotland"
  }'
```

---

### 8. Delete Author
**Endpoint:** `DELETE /authors/{id}`

**Description:** Delete author and cascade delete all associated books

**Path Parameters:**
- `id` (Long, required) - Author ID

**Response (200 OK):**
```
Author deleted successfully
```

**Important:** All books associated with this author will also be deleted!

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/authors/1
```

---

## 📚 Book Endpoints

### 1. Create Book
**Endpoint:** `POST /books/{authorId}`

**Description:** Create a new book for a specific author

**Path Parameters:**
- `authorId` (Long, required) - Author ID

**Request Body:**
```json
{
  "title": "string (required)",
  "isbn": "string (optional, unique)",
  "publicationDate": "YYYY-MM-DD (optional)",
  "genre": "string (optional)",
  "price": "number (optional)"
}
```

**Response (201 Created):**
```json
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

**Error Response (404 Not Found):**
```
Author not found with ID: 999
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Harry Potter and the Philosopher'"'"'s Stone",
    "isbn": "978-0747532699",
    "publicationDate": "1997-06-26",
    "genre": "Fantasy",
    "price": 15.99
  }'
```

---

### 2. Get All Books
**Endpoint:** `GET /books`

**Description:** Retrieve all books

**Response (200 OK):**
```json
[
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
]
```

**Example:**
```bash
curl http://localhost:8080/api/books
```

---

### 3. Get Book by ID
**Endpoint:** `GET /books/{id}`

**Description:** Retrieve a specific book

**Path Parameters:**
- `id` (Long, required) - Book ID

**Response (200 OK):**
```json
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

**Example:**
```bash
curl http://localhost:8080/api/books/1
```

---

### 4. Get Books by Author ID
**Endpoint:** `GET /books/author/{authorId}`

**Description:** Retrieve all books written by a specific author

**Path Parameters:**
- `authorId` (Long, required) - Author ID

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Harry Potter and the Philosopher's Stone",
    "isbn": "978-0747532699",
    "publicationDate": "1997-06-26",
    "genre": "Fantasy",
    "price": 15.99,
    "authorId": 1,
    "authorName": "J.K. Rowling"
  },
  {
    "id": 2,
    "title": "Harry Potter and the Chamber of Secrets",
    "isbn": "978-0747538494",
    "publicationDate": "1998-07-02",
    "genre": "Fantasy",
    "price": 16.99,
    "authorId": 1,
    "authorName": "J.K. Rowling"
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/books/author/1
```

---

### 5. Get Books by Genre
**Endpoint:** `GET /books/genre/{genre}`

**Description:** Retrieve all books of a specific genre

**Path Parameters:**
- `genre` (String, required) - Genre name

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Harry Potter and the Philosopher's Stone",
    "genre": "Fantasy",
    "price": 15.99,
    "authorId": 1,
    "authorName": "J.K. Rowling"
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/books/genre/Fantasy
```

---

### 6. Search Books by Title
**Endpoint:** `GET /books/search/{title}`

**Description:** Search books by title (case-insensitive partial match)

**Path Parameters:**
- `title` (String, required) - Search term

**Response (200 OK):**
```json
[
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
]
```

**Example:**
```bash
curl "http://localhost:8080/api/books/search/Harry"
```

---

### 7. Get Expensive Books
**Endpoint:** `GET /books/expensive`

**Description:** Retrieve books with price above average

**Response (200 OK):**
```json
[
  {
    "id": 3,
    "title": "Harry Potter and the Prisoner of Azkaban",
    "price": 17.99,
    "authorId": 1,
    "authorName": "J.K. Rowling"
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/books/expensive
```

---

### 8. Get Books by Price Range
**Endpoint:** `GET /books/price-range`

**Description:** Retrieve books within a specific price range

**Query Parameters:**
- `minPrice` (Double, required) - Minimum price
- `maxPrice` (Double, required) - Maximum price

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Harry Potter and the Philosopher's Stone",
    "price": 15.99,
    "authorId": 1,
    "authorName": "J.K. Rowling"
  }
]
```

**Example:**
```bash
curl "http://localhost:8080/api/books/price-range?minPrice=10&maxPrice=20"
```

---

### 9. Update Book
**Endpoint:** `PUT /books/{id}`

**Description:** Update book details

**Path Parameters:**
- `id` (Long, required) - Book ID

**Request Body:**
```json
{
  "title": "string",
  "isbn": "string",
  "publicationDate": "YYYY-MM-DD",
  "genre": "string",
  "price": "number"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Harry Potter and the Philosopher's Stone (Updated)",
  "isbn": "978-0747532699",
  "publicationDate": "1997-06-26",
  "genre": "Fantasy",
  "price": 18.99,
  "authorId": 1,
  "authorName": "J.K. Rowling"
}
```

**Example:**
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Harry Potter and the Philosopher'"'"'s Stone",
    "genre": "Fantasy",
    "price": 18.99
  }'
```

---

### 10. Delete Book
**Endpoint:** `DELETE /books/{id}`

**Description:** Delete a specific book

**Path Parameters:**
- `id` (Long, required) - Book ID

**Response (200 OK):**
```
Book deleted successfully
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/books/1
```

---

### 11. Delete All Books by Author
**Endpoint:** `DELETE /books/author/{authorId}`

**Description:** Delete all books written by a specific author

**Path Parameters:**
- `authorId` (Long, required) - Author ID

**Response (200 OK):**
```
All books for author deleted successfully
```

**Important:** This deletes only the books, not the author!

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/books/author/1
```

---

## 🔄 Complete CRUD Workflow Example

### Step 1: Create Author
```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"name":"George Orwell","email":"george@example.com","country":"United Kingdom"}'
```
Response: `{"id": 1, ...}`

### Step 2: Create Books
```bash
curl -X POST http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"1984","isbn":"978-0451524935","genre":"Dystopian","price":13.99}'
```

### Step 3: Read Author with Books
```bash
curl http://localhost:8080/api/authors/1
```

### Step 4: Update Book
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"1984","price":14.99}'
```

### Step 5: Delete Book
```bash
curl -X DELETE http://localhost:8080/api/books/1
```

### Step 6: Delete Author (cascades delete remaining books)
```bash
curl -X DELETE http://localhost:8080/api/authors/1
```

---

## ✅ Response Status Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Successful GET, PUT, or DELETE |
| 201 | Created - Successful POST |
| 400 | Bad Request - Invalid input |
| 404 | Not Found - Resource doesn't exist |
| 409 | Conflict - Duplicate ISBN |
| 500 | Internal Server Error |

---

## ⚠️ Important Notes

1. **Cascade Delete**: Deleting an author will automatically delete all associated books
2. **Orphan Removal**: Removing a book from an author's list will delete it
3. **Foreign Key**: Book requires a valid `authorId`
4. **Unique ISBN**: ISBN must be unique across all books
5. **Lazy Loading**: Author-Book relationship uses lazy loading for performance

