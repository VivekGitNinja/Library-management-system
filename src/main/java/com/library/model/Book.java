package com.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Book implements Serializable {
    private Long id;
    private String bookCode;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private int totalCopies;
    private int availableCopies;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Book() {
    }

    public Book(Long id, String bookCode, String title, String author, String isbn, String genre, int totalCopies, int availableCopies) {
        this.id = id;
        this.bookCode = bookCode;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.isAvailable = availableCopies > 0;
    }

    public Book(String bookCode, String title, String author, String isbn, String genre) {
        this(null, bookCode, title, author, isbn, genre, 1, 1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
        this.isAvailable = availableCopies > 0;
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return String.format("Book[ID=%d, Code='%s', Title='%s', Author='%s', ISBN='%s', Genre='%s', AvailableCopies=%d/%d]",
                id, bookCode, title, author, isbn, genre, availableCopies, totalCopies);
    }
}
