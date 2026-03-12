package com.example.onetomany.controller;

import com.example.onetomany.dto.BookDTO;
import com.example.onetomany.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Book operations
 * Handles HTTP requests for CRUD operations on Book entity
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    /**
     * POST /api/books/{authorId} - Create a new book for an author
     * @param authorId - Author ID (path variable)
     * Request body: BookDTO
     * Response: Created BookDTO with ID
     */
    @PostMapping("/{authorId}")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO, @PathVariable Long authorId) {
        logger.info("POST request to create book: {} for author ID: {}", bookDTO.getTitle(), authorId);
        BookDTO createdBook = bookService.createBook(bookDTO, authorId);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    /**
     * GET /api/books - Retrieve all books
     * Response: List of BookDTOs
     */
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        logger.info("GET request to fetch all books");
        List<BookDTO> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * GET /api/books/{id} - Retrieve book by ID
     * @param id - Book ID
     * Response: BookDTO with author information
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        logger.info("GET request to fetch book with ID: {}", id);
        BookDTO book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    /**
     * GET /api/books/author/{authorId} - Retrieve all books by author ID
     * @param authorId - Author ID
     * Response: List of BookDTOs
     */
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookDTO>> getBooksByAuthorId(@PathVariable Long authorId) {
        logger.info("GET request to fetch books for author ID: {}", authorId);
        List<BookDTO> books = bookService.getBooksByAuthorId(authorId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * GET /api/books/genre/{genre} - Retrieve books by genre
     * @param genre - Book genre
     * Response: List of BookDTOs
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<BookDTO>> getBooksByGenre(@PathVariable String genre) {
        logger.info("GET request to fetch books by genre: {}", genre);
        List<BookDTO> books = bookService.getBooksByGenre(genre);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * GET /api/books/search/{title} - Search books by title (partial match)
     * @param title - Book title (partial or full)
     * Response: List of BookDTOs
     */
    @GetMapping("/search/{title}")
    public ResponseEntity<List<BookDTO>> searchBooksByTitle(@PathVariable String title) {
        logger.info("GET request to search books with title containing: {}", title);
        List<BookDTO> books = bookService.searchBooksByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * GET /api/books/expensive - Retrieve expensive books (above average price)
     * Response: List of BookDTOs
     */
    @GetMapping("/expensive")
    public ResponseEntity<List<BookDTO>> getExpensiveBooks() {
        logger.info("GET request to fetch expensive books");
        List<BookDTO> books = bookService.getExpensiveBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * GET /api/books/price-range - Get books within a price range
     * Query parameters: minPrice, maxPrice
     * Response: List of BookDTOs
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<BookDTO>> getBooksByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        logger.info("GET request to fetch books by price range: {} - {}", minPrice, maxPrice);
        List<BookDTO> books = bookService.getBooksByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * PUT /api/books/{id} - Update book details
     * @param id - Book ID
     * Request body: BookDTO with updated details
     * Response: Updated BookDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        logger.info("PUT request to update book with ID: {}", id);
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    /**
     * DELETE /api/books/{id} - Delete book by ID
     * @param id - Book ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        logger.info("DELETE request to delete book with ID: {}", id);
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
    }

    /**
     * DELETE /api/books/author/{authorId} - Delete all books by author ID
     * @param authorId - Author ID
     */
    @DeleteMapping("/author/{authorId}")
    public ResponseEntity<String> deleteBooksByAuthorId(@PathVariable Long authorId) {
        logger.info("DELETE request to delete all books for author ID: {}", authorId);
        bookService.deleteBooksByAuthorId(authorId);
        return new ResponseEntity<>("All books for author deleted successfully", HttpStatus.OK);
    }
}
