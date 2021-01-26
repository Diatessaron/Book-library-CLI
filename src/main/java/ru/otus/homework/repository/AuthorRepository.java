package ru.otus.homework.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Author;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String> {
    List<Author> findByName(String name);

    void deleteByName(String name);
}
