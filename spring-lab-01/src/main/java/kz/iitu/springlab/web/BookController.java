package kz.iitu.springlab.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @GetMapping("/books")
    public String getBooks() {
        return "Кітаптар тізімі (USER және ADMIN рөлдеріне рұқсат етілген)";
    }

    @PostMapping("/books")
    public String createBook() {
        return "Кітап сәтті қосылды (Тек ADMIN рөліне рұқсат етілген)";
    }
}