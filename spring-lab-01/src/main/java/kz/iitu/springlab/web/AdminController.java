package kz.iitu.springlab.web;

import kz.iitu.springlab.domain.AppUser;
import kz.iitu.springlab.domain.AppUserRepository;
import kz.iitu.springlab.domain.Author;
import kz.iitu.springlab.domain.AuthorRepository;
import kz.iitu.springlab.domain.Book;
import kz.iitu.springlab.domain.BookRepository;
import kz.iitu.springlab.service.BookNotFoundException;
import kz.iitu.springlab.service.BookService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppUserRepository users;
    private final BookRepository books;
    private final AuthorRepository authors;
    private final BookService bookService;

    public AdminController(AppUserRepository users, BookRepository books,
                           AuthorRepository authors, BookService bookService) {
        this.users = users;
        this.books = books;
        this.authors = authors;
        this.bookService = bookService;
    }

    @GetMapping
    public String panel(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("users", users.findAll(Sort.by("id")));
        model.addAttribute("books", books.findAllWithAuthor());
        model.addAttribute("genres", Genre.values());
        return "admin";
    }

    @PostMapping("/users/{id}/toggle")
    @Transactional
    public String toggleUser(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        AppUser u = users.findById(id).orElse(null);
        if (u == null) {
            ra.addFlashAttribute("error", "Қолданушы табылмады");
        } else if (u.getUsername().equals(auth.getName())) {
            ra.addFlashAttribute("error", "Өз аккаунтыңызды өшіре алмайсыз");
        } else {
            u.setEnabled(!u.isEnabled());
            ra.addFlashAttribute("message", "Қолданушы '" + u.getUsername() + "': "
                    + (u.isEnabled() ? "қосылды" : "өшірілді"));
        }
        return "redirect:/admin";
    }

    @PostMapping("/books")
    @Transactional
    public String addBook(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam int year,
                          @RequestParam Genre genre,
                          RedirectAttributes ra) {
        if (title.isBlank() || title.length() > 120 || author.isBlank() || year < 1450 || year > 2100) {
            ra.addFlashAttribute("error", "Деректер дұрыс емес (атауы ≤120 таңба, жылы 1450–2100)");
            return "redirect:/admin";
        }
        Author a = authors.findByName(author.trim())
                .orElseGet(() -> authors.save(new Author(author.trim(), null)));
        Book b = new Book(title.trim(), year, genre);
        a.addBook(b);
        books.save(b);
        ra.addFlashAttribute("message", "Кітап қосылды: " + b.getTitle());
        return "redirect:/admin";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable long id, RedirectAttributes ra) {
        try {
            bookService.delete(id); // @PreAuthorize("hasRole('ADMIN')") осы жерде тексеріледі
            ra.addFlashAttribute("message", "Кітап жойылды");
        } catch (BookNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}