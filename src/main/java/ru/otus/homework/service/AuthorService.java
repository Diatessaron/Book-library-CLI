package ru.otus.homework.service;

import ru.otus.homework.domain.Author;

import java.util.List;

public interface AuthorService {
    String saveAuthor(String name);

    Author getAuthorByName(String name);

    List<Author> getAll();

    String updateAuthor(String oldAuthorName, String name);

    String deleteAuthorByName(String name);
}
