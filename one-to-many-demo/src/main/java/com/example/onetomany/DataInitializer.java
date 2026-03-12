package com.example.onetomany;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.onetomany.entity.Author;
import com.example.onetomany.entity.Book;
import com.example.onetomany.repository.AuthorRepository;
import com.example.onetomany.repository.BookRepository;

/**
 * Data initialization component that populates sample data on application startup
 * Implements CommandLineRunner to run after Spring Boot startup
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        //Check if data already exists
        if (authorRepository.count() > 0) {
            logger.info("Sample data already exists, skipping initialization");
            return;
        }

        logger.info("Initializing sample data...");

        // Create Author 1: J.K. Rowling
        Author author1 = new Author();
        author1.setName("J.K. Rowling");
        author1.setEmail("jk.rowling@example.com");
        author1.setCountry("United Kingdom");
        author1 = authorRepository.save(author1);

        // Create Books for Author 1
        Book book1 = new Book();
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setIsbn("978-0747532699");
        book1.setPublicationDate(LocalDate.of(1997, 6, 26));
        book1.setGenre("Fantasy");
        book1.setPrice(15.99);
        book1.setAuthor(author1);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Harry Potter and the Chamber of Secrets");
        book2.setIsbn("978-0747538494");
        book2.setPublicationDate(LocalDate.of(1998, 7, 2));
        book2.setGenre("Fantasy");
        book2.setPrice(16.99);
        book2.setAuthor(author1);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("Harry Potter and the Prisoner of Azkaban");
        book3.setIsbn("978-0747542155");
        book3.setPublicationDate(LocalDate.of(1999, 7, 8));
        book3.setGenre("Fantasy");
        book3.setPrice(17.99);
        book3.setAuthor(author1);
        bookRepository.save(book3);

        // Create Author 2: Stephen King
        Author author2 = new Author();
        author2.setName("Stephen King");
        author2.setEmail("stephen.king@example.com");
        author2.setCountry("United States");
        author2 = authorRepository.save(author2);

        // Create Books for Author 2
        Book book4 = new Book();
        book4.setTitle("The Shining");
        book4.setIsbn("978-0385333312");
        book4.setPublicationDate(LocalDate.of(1977, 1, 28));
        book4.setGenre("Horror");
        book4.setPrice(12.99);
        book4.setAuthor(author2);
        bookRepository.save(book4);

        Book book5 = new Book();
        book5.setTitle("It");
        book5.setIsbn("978-0451169518");
        book5.setPublicationDate(LocalDate.of(1986, 9, 15));
        book5.setGenre("Horror");
        book5.setPrice(18.99);
        book5.setAuthor(author2);
        bookRepository.save(book5);

        // Create Author 3: George R. R. Martin
        Author author3 = new Author();
        author3.setName("George R. R. Martin");
        author3.setEmail("george.martin@example.com");
        author3.setCountry("United States");
        author3 = authorRepository.save(author3);

        // Create Books for Author 3
        Book book6 = new Book();
        book6.setTitle("A Game of Thrones");
        book6.setIsbn("978-0553103540");
        book6.setPublicationDate(LocalDate.of(1996, 8, 1));
        book6.setGenre("Fantasy");
        book6.setPrice(19.99);
        book6.setAuthor(author3);
        bookRepository.save(book6);

        Book book7 = new Book();
        book7.setTitle("A Clash of Kings");
        book7.setIsbn("978-0553108033");
        book7.setPublicationDate(LocalDate.of(1998, 11, 16));
        book7.setGenre("Fantasy");
        book7.setPrice(19.99);
        book7.setAuthor(author3);
        bookRepository.save(book7);

        logger.info("Sample data initialization completed!");
        logger.info("Total Authors: {}", authorRepository.count());
        logger.info("Total Books: {}", bookRepository.count());
    }
}
