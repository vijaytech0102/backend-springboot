package com.example.onetomany.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.onetomany.entity.Author;

/**
 * Repository for Author entity
 * Extends JpaRepository to get built-in CRUD operations
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Find author by name
     */
    Optional<Author> findByName(String name);

    /**
     * Find all authors from a specific country
     */
    List<Author> findByCountry(String country);

    /**
     * Find author by email
     */
    Optional<Author> findByEmail(String email);

    /**
     * Custom query to find authors with at least N books
     */
    @Query("SELECT a FROM Author a WHERE SIZE(a.books) >= :minBooks")
    List<Author> findAuthorsWithAtLeastNBooks(@Param("minBooks") int minBooks);

    /**
     * Custom query to fetch authors with their books (eager loading)
     */
    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books WHERE a.country = :country")
    List<Author> findAuthorsByCountryWithBooks(@Param("country") String country);
}
