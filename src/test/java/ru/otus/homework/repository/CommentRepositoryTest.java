package ru.otus.homework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository repository;
    final Book ulysses = new Book("Ulysses", new Author("James Joyce"),
            new Genre("Modernist novel"));

    @BeforeEach
    void setUp() {
        repository.save(new Comment("Published in 1922", ulysses));
    }

    @Test
    void shouldReturnCorrectBookByComment() {
        final Book actual = repository.findBookByComment("Published in 1922").orElseThrow
                (() -> new IllegalArgumentException("Incorrect comment content")).getBook();

        assertEquals(ulysses, actual);
    }
}
