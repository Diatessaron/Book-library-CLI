package ru.otus.homework.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository repository;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Book ulysses = new Book(1L, "Ulysses", new Author(1L, "James Joyce"),
                new Genre(1L, "Modernist novel"));
        final Comment expected = new Comment(1L, "Published in 1975", ulysses);

        repository.update(expected.getContent(), expected.getBook(), expected.getId());

        final Comment actual = repository.findById(1L).orElseThrow(() ->
                new IllegalArgumentException("Incorrect id"));

        assertEquals(expected, actual);
    }
}
