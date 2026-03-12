package com.example.onetomany.service;

import com.example.onetomany.dto.AuthorDTO;
import com.example.onetomany.dto.BookDTO;
import com.example.onetomany.entity.Author;
import com.example.onetomany.entity.Book;
import com.example.onetomany.repository.AuthorRepository;
import com.example.onetomany.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Author and Book services
 * Tests the One-to-Many relationship between Author and Book
 */
@SpringBootTest
@ActiveProfiles("test")
public class AuthorBookServiceTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        // Clear all data before each test
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void testCreateAuthor() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("J.K. Rowling");
        authorDTO.setEmail("jk@example.com");
        authorDTO.setCountry("United Kingdom");

        // Act
        AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);

        // Assert
        assertNotNull(createdAuthor.getId());
        assertEquals("J.K. Rowling", createdAuthor.getName());
        assertEquals("jk@example.com", createdAuthor.getEmail());
    }

    @Test
    public void testCreateBookForAuthor() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Stephen King");
        authorDTO.setEmail("stephen@example.com");
        authorDTO.setCountry("United States");
        AuthorDTO author = authorService.createAuthor(authorDTO);

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("The Shining");
        bookDTO.setIsbn("978-0385333312");
        bookDTO.setPublicationDate(LocalDate.of(1977, 1, 28));
        bookDTO.setGenre("Horror");
        bookDTO.setPrice(12.99);

        // Act
        BookDTO createdBook = bookService.createBook(bookDTO, author.getId());

        // Assert
        assertNotNull(createdBook.getId());
        assertEquals("The Shining", createdBook.getTitle());
        assertEquals(author.getId(), createdBook.getAuthorId());
    }

    @Test
    public void testAuthorWithMultipleBooks() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("George R. R. Martin");
        authorDTO.setEmail("george@example.com");
        authorDTO.setCountry("United States");
        AuthorDTO author = authorService.createAuthor(authorDTO);

        // Create multiple books
        BookDTO book1 = new BookDTO();
        book1.setTitle("A Game of Thrones");
        book1.setGenre("Fantasy");
        book1.setPrice(19.99);
        bookService.createBook(book1, author.getId());

        BookDTO book2 = new BookDTO();
        book2.setTitle("A Clash of Kings");
        book2.setGenre("Fantasy");
        book2.setPrice(19.99);
        bookService.createBook(book2, author.getId());

        // Act
        AuthorDTO retrievedAuthor = authorService.getAuthorById(author.getId());
        List<BookDTO> booksByAuthorId = bookService.getBooksByAuthorId(author.getId());

        // Assert
        assertEquals(2, booksByAuthorId.size());
        assertEquals(2, retrievedAuthor.getBooks().size());
    }

    @Test
    public void testDeleteAuthor() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Author to Delete");
        authorDTO.setEmail("delete@example.com");
        authorDTO.setCountry("USA");
        AuthorDTO author = authorService.createAuthor(authorDTO);

        // Add a book
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Book to Delete");
        bookDTO.setGenre("Fiction");
        bookDTO.setPrice(10.0);
        bookService.createBook(bookDTO, author.getId());

        // Act
        authorService.deleteAuthor(author.getId());

        // Assert
        assertEquals(0, authorRepository.count());
        // Due to cascade delete, books should also be deleted
        assertEquals(0, bookRepository.count());
    }

    @Test
    public void testGetBooksByGenre() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Test Author");
        authorDTO.setCountry("USA");
        AuthorDTO author = authorService.createAuthor(authorDTO);

        BookDTO book1 = new BookDTO();
        book1.setTitle("Fantasy Book");
        book1.setGenre("Fantasy");
        book1.setPrice(15.99);
        bookService.createBook(book1, author.getId());

        BookDTO book2 = new BookDTO();
        book2.setTitle("Horror Book");
        book2.setGenre("Horror");
        book2.setPrice(12.99);
        bookService.createBook(book2, author.getId());

        // Act
        List<BookDTO> fantasyBooks = bookService.getBooksByGenre("Fantasy");

        // Assert
        assertEquals(1, fantasyBooks.size());
        assertEquals("Fantasy Book", fantasyBooks.get(0).getTitle());
    }

    @Test
    public void testSearchBooksByTitle() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Robert Tolkien");
        authorDTO.setCountry("England");
        AuthorDTO author = authorService.createAuthor(authorDTO);

        BookDTO book1 = new BookDTO();
        book1.setTitle("The Lord of the Rings");
        book1.setGenre("Fantasy");
        book1.setPrice(20.0);
        bookService.createBook(book1, author.getId());

        BookDTO book2 = new BookDTO();
        book2.setTitle("The Hobbit");
        book2.setGenre("Fantasy");
        book2.setPrice(15.0);
        bookService.createBook(book2, author.getId());

        // Act
        List<BookDTO> results = bookService.searchBooksByTitle("Lord");

        // Assert
        assertEquals(1, results.size());
        assertEquals("The Lord of the Rings", results.get(0).getTitle());
    }

    @Test
    public void testUpdateBook() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Author");
        authorDTO.setCountry("USA");
        AuthorDTO author = authorService.createAuthor(authorDTO);

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Original Title");
        bookDTO.setGenre("Fiction");
        bookDTO.setPrice(10.0);
        BookDTO createdBook = bookService.createBook(bookDTO, author.getId());

        // Act
        bookDTO.setTitle("Updated Title");
        bookDTO.setPrice(12.0);
        BookDTO updatedBook = bookService.updateBook(createdBook.getId(), bookDTO);

        // Assert
        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals(12.0, updatedBook.getPrice());
    }

    @Test
    public void testGetAuthorsByCountry() {
        // Arrange
        AuthorDTO author1 = new AuthorDTO();
        author1.setName("Author 1");
        author1.setCountry("USA");
        authorService.createAuthor(author1);

        AuthorDTO author2 = new AuthorDTO();
        author2.setName("Author 2");
        author2.setCountry("USA");
        authorService.createAuthor(author2);

        AuthorDTO author3 = new AuthorDTO();
        author3.setName("Author 3");
        author3.setCountry("UK");
        authorService.createAuthor(author3);

        // Act
        List<AuthorDTO> usAuthors = authorService.getAuthorsByCountry("USA");
        List<AuthorDTO> ukAuthors = authorService.getAuthorsByCountry("UK");

        // Assert
        assertEquals(2, usAuthors.size());
        assertEquals(1, ukAuthors.size());
    }
}
