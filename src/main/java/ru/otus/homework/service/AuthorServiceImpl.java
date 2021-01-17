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
        final Author author = new Author(0L, name);
        authorRepository.save(author);
        return String.format("You successfully saved a %s to repository", author.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public Author getAuthorById(long id) {
        return authorRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id"));
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
    public String updateAuthor(long id, String name) {
        final Author author = new Author(id, String.join(" ", name.split(",")));
        authorRepository.update(author.getName(), author.getId());

        return String.format("%s was updated", author.getName());
    }

    @Transactional
    @Override
    public String deleteAuthorById(long id) {
        final Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id"));
        authorRepository.deleteById(id);

        return String.format("%s was deleted", author.getName());
    }
}
