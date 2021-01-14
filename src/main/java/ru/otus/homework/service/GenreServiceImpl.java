package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.GenreRepository;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional
    @Override
    public String saveGenre(String name) {
        final Genre genre = new Genre(0L, name);
        genreRepository.save(genre);

        return String.format("You successfully saved a %s to repository", genre.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenreById(long id) {
        return genreRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect id"));
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenreByName(String name) {
        return genreRepository.findByName(name).orElseThrow
                (() -> new IllegalArgumentException("Incorrect name"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Override
    public String updateGenre(long id, String name) {
        genreRepository.update(name, id);

        return String.format("%s was updated", name);
    }

    @Transactional
    @Override
    public String deleteGenreById(long id) {
        final Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id"));
        genreRepository.deleteById(id);

        return String.format("%s was deleted", genre.getName());
    }
}
