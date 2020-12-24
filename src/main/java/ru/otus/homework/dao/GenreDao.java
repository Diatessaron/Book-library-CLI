package ru.otus.homework.dao;

import ru.otus.homework.domain.Genre;

import java.util.List;

public interface GenreDao {
    int count();
    void insert(Genre genre);
    Genre getGenreById(long id);
    Genre getGenreByName(String name);
    List<Genre> getAll();
    void update(Genre genre);
    void deleteById(long id);
}
