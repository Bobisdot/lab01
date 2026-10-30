package kz.iitu.springlab.service;

import kz.iitu.springlab.domain.Book;
import kz.iitu.springlab.domain.BookRepository;
import kz.iitu.springlab.web.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // Бұл сессияның ашық болуын қамтамасыз етеді
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(String authorName) {
        if (authorName == null || authorName.isBlank()) {
            return bookRepository.findAll();
        }
        return bookRepository.findAll();
    }

    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Transactional // Сақтау үшін транзакция керек
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(long id, Book book) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        book.setId(id);
        return bookRepository.save(book);
    }

    @Transactional
    public void delete(long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    // === 4-тапсырма үшін жаңа әдіс ===
    public Page<Book> find(Genre genre, Pageable pageable) {
        if (genre == null) {
            return bookRepository.findAll(pageable); // @EntityGraph қосылған әдіс
        }
        return bookRepository.findByGenre(genre, pageable); // @EntityGraph қосылған әдіс
    }
}