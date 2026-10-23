package kz.iitu.springlab.service;

import kz.iitu.springlab.domain.Book;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {
    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public List<Book> findAll(String author) {
        if (author == null || author.isBlank()) {
            return new ArrayList<>(books.values());
        }
        return books.values().stream()
                .filter(b -> b.getAuthor() != null && b.getAuthor().equalsIgnoreCase(author))
                .toList();
    }

    public Optional<Book> findById(long id) {
        return Optional.ofNullable(books.get(id));
    }

    public Book create(Book book) {
        long newId = idGenerator.incrementAndGet();
        book.setId(newId);
        books.put(newId, book);
        return book;
    }

    public Book update(long id, Book book) {
        if (!books.containsKey(id)) {
            throw new BookNotFoundException(id);
        }
        book.setId(id);
        books.put(id, book);
        return book;
    }

    public void delete(long id) {
        books.remove(id);
    }
}