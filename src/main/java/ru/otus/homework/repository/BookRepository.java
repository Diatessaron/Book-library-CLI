package ru.otus.homework.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findByTitle(String title);

    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findByAuthor_Name(String author);

    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findByGenre_Name(String genre);

    @Query("select b from Book b join fetch b.genre g join fetch b.author a " +
            "left join fetch Comment c on c.book = b " +
            "where c.content = :content")
    Optional<Book> findByComment_Content(String content);

    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();
}
