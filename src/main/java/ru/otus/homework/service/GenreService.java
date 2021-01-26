package ru.otus.homework.service;

import ru.otus.homework.domain.Genre;

import java.util.List;

public interface GenreService {
    String saveGenre(String name);

    Genre getGenreByName(String name);

    List<Genre> getAll();

    String updateGenre(String oldGenreName, String name);

    String deleteGenreByName(String name);
}
