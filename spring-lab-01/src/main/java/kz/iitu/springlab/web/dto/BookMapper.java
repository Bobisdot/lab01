package kz.iitu.springlab.web.dto;

import kz.iitu.springlab.domain.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    public Book toDomain(BookRequest request) {
        // Назар аударыңыз: біздің жаңа Book конструкторы 3 параметр қабылдайды
        // (title, year, genre). Author-ды кейінірек Service ішінде қосамыз.
        return new Book(
                request.title(),
                request.year(),
                request.genre()
        );
    }

    public BookResponse toResponse(Book book) {
        // book.getAuthor() енді Author класын қайтарады.
        // Сондықтан біз оның атын .getName() арқылы аламыз.
        // Егер автор null болса, қате шықпауы үшін тексереміз.
        String authorName = (book.getAuthor() != null) ? book.getAuthor().getName() : null;

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                authorName,
                book.getYear(),
                book.getGenre(),
                book.getCreatedAt()
        );
    }

    public List<BookResponse> toResponseList(List<Book> books) {
        return books.stream().map(this::toResponse).toList();
    }
}