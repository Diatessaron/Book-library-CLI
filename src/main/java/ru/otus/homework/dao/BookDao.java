package ru.otus.homework.dao;

import ru.otus.homework.domain.Book;

import java.util.List;

public interface BookDao {
    void insert(Book book);
    Book getBookById(long id);
    Book getBookByTitle(String title);
    Book getBookByAuthor(String author);
    Book getBookByGenre(String genre);
    List<Book> getAll();
    void update(Book book);
    void deleteById(long id);
}
