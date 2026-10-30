package kz.iitu.springlab.domain;

import kz.iitu.springlab.web.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // === 5 DERIVED QUERIES ===
    List<Book> findByGenre(Genre genre);
    List<Book> findByTitleContainingIgnoreCase(String fragment);
    List<Book> findByYearBetweenOrderByYearDesc(int from, int to);
    List<Book> findTop3ByGenreOrderByYearDesc(Genre genre);
    long countByAuthorName(String name);

    // === JPQL QUERY ===
    @Query("""
           select b from Book b
           where b.genre = :genre and b.year >= :year
           order by b.title""")
    List<Book> search(@Param("genre") Genre genre, @Param("year") int year);

    // === BULK UPDATE ===
    @Modifying(clearAutomatically = true)
    @Query("update Book b set b.archived = true where b.year < :year")
    int archiveOlderThan(@Param("year") int year);

    // === N+1 FIX (FETCH JOIN) ===
    @Query("select b from Book b join fetch b.author")
    List<Book> findAllWithAuthor();

    // === PAGINATION (Task 4) ===
    // @EntityGraph арқылы авторды да бірге жүктейміз (LazyInitializationException болмас үшін)
    @EntityGraph(attributePaths = "author")
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "author")
    Page<Book> findByGenre(Genre genre, Pageable pageable);
}