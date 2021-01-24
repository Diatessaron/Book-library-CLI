package ru.otus.homework.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.GenreRepository;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final MongoTemplate mongoTemplate;

    public GenreServiceImpl(GenreRepository genreRepository, MongoTemplate mongoTemplate) {
        this.genreRepository = genreRepository;
        this.mongoTemplate = mongoTemplate;
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
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(oldGenreName));
        Update update = new Update();
        update.set("name", name);
        mongoTemplate.updateFirst(query, update, Genre.class);

        query = new Query();
        query.addCriteria(Criteria.where("genre.name").is(oldGenreName));
        update = new Update();
        update.set("genre.name", name);
        mongoTemplate.updateFirst(query, update, Book.class, "books");

        return String.format("%s was updated", name);
    }

    @Transactional
    @Override
    public String deleteGenreByName(String name) {
        final Genre genre = genreRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect genre name"));
        genreRepository.deleteByName(name);

        Query query = new Query();
        query.addCriteria(Criteria.where("genre.name").is(name));
        mongoTemplate.remove(query, Book.class, "books");

        return String.format("%s was deleted", genre.getName());
    }
}
