package com.example.onetomany.service;

import com.example.onetomany.dto.BookDTO;
import com.example.onetomany.entity.Author;
import com.example.onetomany.entity.Book;
import com.example.onetomany.repository.AuthorRepository;
import com.example.onetomany.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Book entity
 * Handles business logic for Book operations
 */
@Service
@Transactional
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    /**
     * Create a new book for an author
     */
    public BookDTO createBook(BookDTO bookDTO, Long authorId) {
        logger.info("Creating new book: {} for Author ID: {}", bookDTO.getTitle(), authorId);

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + authorId));

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setGenre(bookDTO.getGenre());
        book.setPrice(bookDTO.getPrice());
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        logger.info("Book created with ID: {}", savedBook.getId());
        return convertToDTO(savedBook);
    }

    /**
     * Retrieve all books
     */
    public List<BookDTO> getAllBooks() {
        logger.info("Fetching all books");
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve book by ID
     */
    public BookDTO getBookById(Long id) {
        logger.info("Fetching book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
        return convertToDTO(book);
    }

    /**
     * Retrieve all books by author ID
     */
    public List<BookDTO> getBooksByAuthorId(Long authorId) {
        logger.info("Fetching books for Author ID: {}", authorId);
        // Verify author exists
        authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + authorId));

        return bookRepository.findByAuthorId(authorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve books by genre
     */
    public List<BookDTO> getBooksByGenre(String genre) {
        logger.info("Fetching books by genre: {}", genre);
        return bookRepository.findByGenre(genre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve books by title (partial match)
     */
    public List<BookDTO> searchBooksByTitle(String title) {
        logger.info("Searching books with title containing: {}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update book details
     */
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        logger.info("Updating book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setGenre(bookDTO.getGenre());
        book.setPrice(bookDTO.getPrice());

        Book updatedBook = bookRepository.save(book);
        logger.info("Book updated with ID: {}", id);
        return convertToDTO(updatedBook);
    }

    /**
     * Delete book by ID
     */
    public void deleteBook(Long id) {
        logger.info("Deleting book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
        bookRepository.delete(book);
        logger.info("Book deleted with ID: {}", id);
    }

    /**
     * Delete all books by author ID
     */
    public void deleteBooksByAuthorId(Long authorId) {
        logger.info("Deleting all books for Author ID: {}", authorId);
        List<Book> books = bookRepository.findByAuthorId(authorId);
        bookRepository.deleteAll(books);
        logger.info("All books deleted for Author ID: {}", authorId);
    }

    /**
     * Get expensive books (above average price)
     */
    public List<BookDTO> getExpensiveBooks() {
        logger.info("Fetching expensive books");
        return bookRepository.findExpensiveBooks().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get books by price range
     */
    public List<BookDTO> getBooksByPriceRange(Double minPrice, Double maxPrice) {
        logger.info("Fetching books by price range: {} - {}", minPrice, maxPrice);
        return bookRepository.findByPriceGreaterThan(minPrice).stream()
                .filter(book -> book.getPrice() <= maxPrice)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Book entity to BookDTO
     */
    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setGenre(book.getGenre());
        dto.setPrice(book.getPrice());

        if (book.getAuthor() != null) {
            dto.setAuthorId(book.getAuthor().getId());
            dto.setAuthorName(book.getAuthor().getName());
        }

        return dto;
    }
}
