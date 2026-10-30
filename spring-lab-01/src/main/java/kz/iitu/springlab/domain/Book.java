package kz.iitu.springlab.domain;

import jakarta.persistence.*;
import kz.iitu.springlab.web.Genre;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    // String автордың орнына Author класы
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    private int year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Genre genre;

    private boolean archived;

    // ManyToMany байланысы
    @ManyToMany
    @JoinTable(name = "book_tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    @Version
    private long version;

    public Book() {}

    // Конструктор: author-сыз (өйткені ол кейін addBook арқылы қосылады)
    public Book(String title, int year, Genre genre) {
        this.title = title;
        this.year = year;
        this.genre = genre;
    }

    // Геттерлер мен сеттерлер
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; } // Author.addBook үшін керек

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }

    public boolean isArchived() { return archived; }
    public void setArchived(boolean archived) { this.archived = archived; }

    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) { this.tags = tags; }

    public Instant getCreatedAt() { return createdAt; }
    public void setId(Long id) {
        this.id = id;
    }
}