package kz.iitu.springlab.web.dto;

import kz.iitu.springlab.domain.Book;
import kz.iitu.springlab.web.Genre;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class BookMapper {

    public Book toDomain(BookRequest request) {
        return new Book(
                null,
                request.title(),
                request.author(),
                request.year(),
                request.genre(),
                Instant.now()
        );
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getGenre(),
                book.getCreatedAt()
        );
    }

    public List<BookResponse> toResponseList(List<Book> books) {
        return books.stream().map(this::toResponse).toList();
    }
}