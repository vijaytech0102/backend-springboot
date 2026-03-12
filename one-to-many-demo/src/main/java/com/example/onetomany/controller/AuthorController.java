package com.example.onetomany.controller;

import com.example.onetomany.dto.AuthorDTO;
import com.example.onetomany.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Author operations
 * Handles HTTP requests for CRUD operations on Author entity
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorService authorService;

    /**
     * POST /api/authors - Create a new author
     * Request body: AuthorDTO
     * Response: Created AuthorDTO with ID
     */
    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDTO) {
        logger.info("POST request to create author: {}", authorDTO.getName());
        AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    /**
     * GET /api/authors - Retrieve all authors
     * Response: List of AuthorDTOs
     */
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        logger.info("GET request to fetch all authors");
        List<AuthorDTO> authors = authorService.getAllAuthors();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    /**
     * GET /api/authors/{id} - Retrieve author by ID
     * @param id - Author ID
     * Response: AuthorDTO with all associated books
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        logger.info("GET request to fetch author with ID: {}", id);
        AuthorDTO author = authorService.getAuthorById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    /**
     * GET /api/authors/search/{name} - Retrieve author by name
     * @param name - Author name
     * Response: AuthorDTO
     */
    @GetMapping("/search/{name}")
    public ResponseEntity<AuthorDTO> getAuthorByName(@PathVariable String name) {
        logger.info("GET request to fetch author with name: {}", name);
        AuthorDTO author = authorService.getAuthorByName(name);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    /**
     * GET /api/authors/country/{country} - Retrieve all authors from a country
     * @param country - Country name
     * Response: List of AuthorDTOs
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByCountry(@PathVariable String country) {
        logger.info("GET request to fetch authors from country: {}", country);
        List<AuthorDTO> authors = authorService.getAuthorsByCountry(country);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    /**
     * PUT /api/authors/{id} - Update author details
     * @param id - Author ID
     * Request body: AuthorDTO with updated details
     * Response: Updated AuthorDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        logger.info("PUT request to update author with ID: {}", id);
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }

    /**
     * DELETE /api/authors/{id} - Delete author by ID
     * Note: All associated books will be deleted due to orphanRemoval = true
     * @param id - Author ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        logger.info("DELETE request to delete author with ID: {}", id);
        authorService.deleteAuthor(id);
        return new ResponseEntity<>("Author deleted successfully", HttpStatus.OK);
    }

    /**
     * GET /api/authors/books/{minBooks} - Get authors with at least N books
     * @param minBooks - Minimum number of books
     * Response: List of AuthorDTOs
     */
    @GetMapping("/books/{minBooks}")
    public ResponseEntity<List<AuthorDTO>> getAuthorsWithAtLeastNBooks(@PathVariable int minBooks) {
        logger.info("GET request to fetch authors with at least {} books", minBooks);
        List<AuthorDTO> authors = authorService.getAuthorsWithAtLeastNBooks(minBooks);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }
}
