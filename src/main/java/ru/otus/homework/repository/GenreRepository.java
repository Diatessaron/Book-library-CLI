package ru.otus.homework.repository;

import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    long count();
    Genre save(Genre genre);
    Optional<Genre> getGenreById(long id);
    Genre getGenreByName(String name);
    List<Genre> getAll();
    void update(Genre genre);
    void deleteById(long id);
}
