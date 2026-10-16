package kz.iitu.springlab.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookPageController {

    // Сақталған кітаптарды уақытша жадыда сақтау үшін тізім
    private final List<BookForm> bookList = new ArrayList<>();

    // 1. Барлық кітаптар каталогын көрсететін метод (GET /books)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookList);
        return "books/list";
    }

    // 2. Жаңа кітап қосу формасын ашатын метод (GET /books/new)
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new BookForm());
        model.addAttribute("genres", Genre.values());
        return "books/form";
    }

    // 3. Форманы қабылдап, тізімге сақтайтын метод (POST /books)
    @PostMapping
    public String create(@Valid @ModelAttribute("form") BookForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {

        if (binding.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "books/form";
        }

        // Кітапты тізімге қосамыз
        bookList.add(form);

        // Flash message - бір рет көрсетілетін хабарлама
        redirect.addFlashAttribute("message", "Book «" + form.getTitle() + "» has been added successfully!");
        return "redirect:/books"; // Каталог бетіне бағыттау (Post/Redirect/Get)
    }
}