package com.example.onetomany.service;

import com.example.onetomany.dto.AuthorDTO;
import com.example.onetomany.dto.BookDTO;
import com.example.onetomany.entity.Author;
import com.example.onetomany.entity.Book;
import com.example.onetomany.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Author entity
 * Handles business logic for Author operations
 */
@Service
@Transactional
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    private AuthorRepository authorRepository;

    /**
     * Create a new author
     */
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        logger.info("Creating new author: {}", authorDTO.getName());
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setEmail(authorDTO.getEmail());
        author.setCountry(authorDTO.getCountry());

        Author savedAuthor = authorRepository.save(author);
        logger.info("Author created with ID: {}", savedAuthor.getId());
        return convertToDTO(savedAuthor);
    }

    /**
     * Retrieve all authors
     */
    public List<AuthorDTO> getAllAuthors() {
        logger.info("Fetching all authors");
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve author by ID
     */
    public AuthorDTO getAuthorById(Long id) {
        logger.info("Fetching author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + id));
        return convertToDTO(author);
    }

    /**
     * Retrieve author by name
     */
    public AuthorDTO getAuthorByName(String name) {
        logger.info("Fetching author with name: {}", name);
        Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Author not found with name: " + name));
        return convertToDTO(author);
    }

    /**
     * Update author details
     */
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        logger.info("Updating author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + id));

        author.setName(authorDTO.getName());
        author.setEmail(authorDTO.getEmail());
        author.setCountry(authorDTO.getCountry());

        Author updatedAuthor = authorRepository.save(author);
        logger.info("Author updated with ID: {}", id);
        return convertToDTO(updatedAuthor);
    }

    /**
     * Delete author by ID (including all associated books due to orphanRemoval)
     */
    public void deleteAuthor(Long id) {
        logger.info("Deleting author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + id));
        authorRepository.delete(author);
        logger.info("Author deleted with ID: {}", id);
    }

    /**
     * Get all authors from a specific country
     */
    public List<AuthorDTO> getAuthorsByCountry(String country) {
        logger.info("Fetching authors from country: {}", country);
        return authorRepository.findByCountry(country).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get authors with at least N books
     */
    public List<AuthorDTO> getAuthorsWithAtLeastNBooks(int minBooks) {
        logger.info("Fetching authors with at least {} books", minBooks);
        return authorRepository.findAuthorsWithAtLeastNBooks(minBooks).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Author entity to AuthorDTO
     */
    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setEmail(author.getEmail());
        dto.setCountry(author.getCountry());

        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            dto.setBooks(author.getBooks().stream()
                    .map(this::convertBookToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * Convert Book entity to BookDTO
     */
    private BookDTO convertBookToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setGenre(book.getGenre());
        dto.setPrice(book.getPrice());
        return dto;
    }
}
