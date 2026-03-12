package com.example.onetomany.repository;

import com.example.onetomany.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Book entity
 * Extends JpaRepository to get built-in CRUD operations
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find books by title
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find book by ISBN
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Find all books by a specific author
     */
    List<Book> findByAuthorId(Long authorId);

    /**
     * Find all books by author name
     */
    List<Book> findByAuthorName(String authorName);

    /**
     * Find all books by genre
     */
    List<Book> findByGenre(String genre);

    /**
     * Find books with price greater than specified value
     */
    List<Book> findByPriceGreaterThan(Double price);

    /**
     * Custom query to get books by author with their author details
     */
    @Query("SELECT b FROM Book b WHERE b.author.id = :authorId")
    List<Book> findBooksByAuthorWithDetails(@Param("authorId") Long authorId);

    /**
     * Custom query to find expensive books (above average price)
     */
    @Query("SELECT b FROM Book b WHERE b.price > (SELECT AVG(b2.price) FROM Book b2)")
    List<Book> findExpensiveBooks();
}
