package kz.iitu.springlab.web;

import kz.iitu.springlab.domain.Book;
import kz.iitu.springlab.service.BookService;
import kz.iitu.springlab.web.dto.BookMapper;
import kz.iitu.springlab.web.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookApiController {

    private final BookService service;
    private final BookMapper mapper;

    public BookApiController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // 4-тапсырма: беттеу және сұрыптау
    @GetMapping("/api/books")
    public Page<BookResponse> list(
            @RequestParam(required = false) Genre genre,
            @PageableDefault(size = 5, sort = "title") Pageable pageable) {

        // Service-тен Page<Book> аламыз, оны Page<BookResponse>-ке айналдырамыз
        return service.find(genre, pageable).map(mapper::toResponse);
    }
}