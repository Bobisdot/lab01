package kz.iitu.springlab.domain;

import kz.iitu.springlab.web.Genre;
import java.time.Instant;

public class Book {
    private Long id;
    private String title;
    private String author;
    private int year;
    private Genre genre;
    private Instant createdAt;

    public Book() {}

    public Book(Long id, String title, String author, int year, Genre genre, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}