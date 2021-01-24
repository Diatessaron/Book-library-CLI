package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public GenreServiceImpl(GenreRepository genreRepository, BookRepository bookRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Override
    public String saveGenre(String name) {
        final Genre genre = new Genre(name);
        genreRepository.save(genre);

        return String.format("You successfully saved a %s to repository", genre.getName());
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
    public String updateGenre(String oldGenreName, String name) {
        final Genre genre = genreRepository.findByName(oldGenreName).orElseThrow
                (() -> new IllegalArgumentException("Incorrect genre name"));
        genre.setName(name);
        genreRepository.save(genre);

        final List<Book> bookList = bookRepository.findByGenre_Name(oldGenreName);

        if(!bookList.isEmpty()) {
            bookList.forEach(b -> b.setGenre(genre));
            bookRepository.saveAll(bookList);
        }

        return String.format("%s was updated", name);
    }

    @Transactional
    @Override
    public String deleteGenreByName(String name) {
        final Genre genre = genreRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect genre name"));
        genreRepository.deleteByName(name);
        bookRepository.deleteByGenre_Name(name);

        return String.format("%s was deleted", genre.getName());
    }
}
