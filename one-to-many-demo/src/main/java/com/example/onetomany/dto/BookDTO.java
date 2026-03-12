package com.example.onetomany.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for Book
 * Used to transfer data between layers and API responses
 */
public class BookDTO {

    private Long id;
    private String title;
    private String isbn;
    private LocalDate publicationDate;
    private String genre;
    private Double price;
    private Long authorId;
    private String authorName;

    // Constructors
    public BookDTO() {}

    public BookDTO(Long id, String title, String isbn, LocalDate publicationDate) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
    }

    public BookDTO(Long id, String title, String isbn, LocalDate publicationDate, String genre, Double price) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.price = price;
    }

    public BookDTO(Long id, String title, String isbn, LocalDate publicationDate, String genre, Double price, Long authorId, String authorName) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.price = price;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
