package kz.iitu.springlab;

import kz.iitu.springlab.domain.*;
import kz.iitu.springlab.web.Genre;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final BookRepository bookRepository;

    public DataInitializer(AuthorRepository authorRepository, TagRepository tagRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() > 0) {
            System.out.println("=== Деректер қорында кітаптар бар, қайта қосудың қажеті жоқ ===");
            return;
        }

        // 1. Тегтерді құру
        Tag fantasy = new Tag("Fantasy");
        Tag classic = new Tag("Classic");
        tagRepository.save(fantasy);
        tagRepository.save(classic);

        // 2. Авторларды құру
        Author tolkien = new Author("J.R.R. Tolkien", "UK");
        Author orwell = new Author("George Orwell", "UK");

        // 3. Кітаптарды құру
        Book hobbit = new Book("The Hobbit", 1937, Genre.FANTASY);
        Book lotr = new Book("The Lord of the Rings", 1954, Genre.FANTASY);
        Book animalFarm = new Book("Animal Farm", 1945, Genre.NON_FICTION);

        // Байланыстарды орнату
        tolkien.addBook(hobbit);
        tolkien.addBook(lotr);
        orwell.addBook(animalFarm);

        hobbit.getTags().add(fantasy);
        lotr.getTags().add(fantasy);
        animalFarm.getTags().add(classic);

        authorRepository.save(tolkien);
        authorRepository.save(orwell);

        System.out.println("=== ТЕСТТІК ДЕРЕКТЕР СӘТТІ ҚОСЫЛДЫ ===");

        // 4. Сұраныстарды тексеру
        System.out.println("=== FIND BY GENRE (FANTASY) ===");
        bookRepository.findByGenre(Genre.FANTASY).forEach(b -> System.out.println(b.getTitle()));

        System.out.println("=== FIND BY TITLE CONTAINING 'The' ===");
        bookRepository.findByTitleContainingIgnoreCase("The").forEach(b -> System.out.println(b.getTitle()));

        System.out.println("=== FIND BY YEAR BETWEEN 1900 AND 1950 ===");
        bookRepository.findByYearBetweenOrderByYearDesc(1900, 1950).forEach(b -> System.out.println(b.getTitle() + " - " + b.getYear()));

        System.out.println("=== TOP 3 FANTASY BOOKS ===");
        bookRepository.findTop3ByGenreOrderByYearDesc(Genre.FANTASY).forEach(b -> System.out.println(b.getTitle()));

        System.out.println("=== COUNT BY AUTHOR NAME 'J.R.R. Tolkien' ===");
        System.out.println(bookRepository.countByAuthorName("J.R.R. Tolkien"));

        System.out.println("=== SEARCH JPQL ===");
        bookRepository.search(Genre.FANTASY, 1930).forEach(b -> System.out.println(b.getTitle()));

        // N+1 мәселесін шешу үшін Fetch Join қолдану
        System.out.println("=== FIND ALL WITH AUTHOR (N+1 fix) ===");
        bookRepository.findAllWithAuthor().forEach(b -> System.out.println(b.getTitle() + " by " + b.getAuthor().getName()));
    }

}