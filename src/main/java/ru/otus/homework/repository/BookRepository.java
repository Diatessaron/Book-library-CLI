package ru.otus.homework.repository;

import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> getBookById(long id);
    Book getBookByTitle(String title);
    Book getBookByAuthor(String author);
    Book getBookByGenre(String genre);
    List<Book> getAll();
    void update(Book book);
    void deleteById(long id);
}
