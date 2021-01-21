package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
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
        final Author author = authorRepository.findByName(oldAuthorName).orElseThrow
                (() -> new IllegalArgumentException("Incorrect author name"));
        author.setName(name);

        authorRepository.deleteByName(oldAuthorName);
        authorRepository.save(author);

        return String.format("%s was updated", name);
    }

    @Transactional
    @Override
    public String deleteAuthorByName(String name) {
        final Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect name"));
        authorRepository.deleteByName(name);

        return String.format("%s was deleted", author.getName());
    }
}
