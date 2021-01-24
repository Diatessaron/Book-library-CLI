package ru.otus.homework.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final MongoTemplate mongoTemplate;

    public AuthorServiceImpl(AuthorRepository authorRepository, MongoTemplate mongoTemplate) {
        this.authorRepository = authorRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    @Override
    public String saveAuthor(String name) {
        final Author author = new Author(name);
        authorRepository.save(author);
        return String.format("You successfully saved a %s to repository", author.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public Author getAuthorByName(String name) {
        return authorRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect name"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    public String updateAuthor(String oldAuthorName, String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(oldAuthorName));
        Update update = new Update();
        update.set("name", name);
        mongoTemplate.updateFirst(query, update, Author.class);

        query = new Query();
        query.addCriteria(Criteria.where("author.name").is(oldAuthorName));
        update = new Update();
        update.set("author.name", name);
        mongoTemplate.updateFirst(query, update, Book.class, "books");

        return String.format("%s was updated", name);
    }

    @Transactional
    @Override
    public String deleteAuthorByName(String name) {
        final Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect name"));
        authorRepository.deleteByName(name);

        Query query = new Query();
        query.addCriteria(Criteria.where("author.name").is(name));
        mongoTemplate.remove(query, Book.class, "books");

        return String.format("%s was deleted", author.getName());
    }
}
