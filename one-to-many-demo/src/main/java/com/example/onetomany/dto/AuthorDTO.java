package com.example.onetomany.dto;

import java.util.List;

/**
 * Data Transfer Object for Author
 * Used to transfer data between layers and API responses
 */
public class AuthorDTO {

    private Long id;
    private String name;
    private String email;
    private String country;
    private List<BookDTO> books;

    // Constructors
    public AuthorDTO() {}

    public AuthorDTO(Long id, String name, String email, String country) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country = country;
    }

    public AuthorDTO(Long id, String name, String email, String country, List<BookDTO> books) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country = country;
        this.books = books;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }
}
