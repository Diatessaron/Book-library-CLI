package ru.otus.homework.dao;

import ru.otus.homework.domain.Author;

import java.util.List;

public interface AuthorDao {
    int count();
    void insert(Author author);
    Author getAuthorById(long id);
    Author getAuthorByName(String name);
    List<Author> getAll();
    void update(Author author);
    void deleteById(long id);
}
