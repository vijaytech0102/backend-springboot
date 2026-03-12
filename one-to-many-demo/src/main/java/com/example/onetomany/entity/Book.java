package com.example.onetomany.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Book Entity - Child side of One-to-Many relationship
 * Many Books belong to One Author
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "genre")
    private String genre;

    @Column(name = "price")
    private Double price;

    /**
     * ManyToOne relationship - Many Books belong to One Author
     * This is the owning side of the relationship
     * @JoinColumn specifies the foreign key column name in the books table
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    // Constructors
    public Book() {}

    /**
     * Constructor for creating Book with basic details
     */
    public Book(String title, String isbn, LocalDate publicationDate) {
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
    }

    public Book(String title, String isbn, LocalDate publicationDate, String genre, Double price) {
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.price = price;
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
