package com.example.onetomany.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Author Entity - Parent side of One-to-Many relationship
 * One Author can have Many Books
 */
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "country")
    private String country;

    /**
     * OneToMany relationship - One Author can write Many Books
     * mappedBy: refers to the field name in Book entity that owns the relationship
     * cascade: defines how operations on Author affect associated Books
     * orphanRemoval: when a Book is removed from the list, it will be deleted from DB
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    // Constructors
    public Author() {}

    public Author(String name, String email, String country) {
        this.name = name;
        this.email = email;
        this.country = country;
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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Add a book to the author's list
     */
    public void addBook(Book book) {
        if (books == null) {
            books = new ArrayList<>();
        }
        books.add(book);
        book.setAuthor(this);
    }

    /**
     * Remove a book from the author's list
     */
    public void removeBook(Book book) {
        if (books != null) {
            books.remove(book);
            book.setAuthor(null);
        }
    }
}
