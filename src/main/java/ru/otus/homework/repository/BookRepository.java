package ru.otus.homework.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByTitle(String title);

    Optional<Book> findByAuthor_Name(String author);

    Optional<Book> findByGenre_Name(String genre);

    List<Book> findAll();

    void deleteByTitle(String title);
}
