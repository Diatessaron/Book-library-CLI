package ru.otus.homework.service;

import ru.otus.homework.domain.Genre;

import java.util.List;

public interface GenreService {
    String saveGenre(String name);

    Genre getGenreById(long id);

    Genre getGenreByName(String name);

    List<Genre> getAll();

    String updateGenre(long id, String name);

    String deleteGenreById(long id);
}
