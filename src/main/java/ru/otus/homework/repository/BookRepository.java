package ru.otus.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.homework.domain.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);

    Optional<Book> findByAuthor_Name(String author);

    Optional<Book> findByGenre_Name(String genre);

    @Query("select b from Book b join fetch b.genre g join fetch b.author a " +
            "left join fetch Comment c on c.book = b " +
            "where c.content = :content")
    Optional<Book> findByComment_Content(String content);
}
