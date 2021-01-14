package ru.otus.homework.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Genre;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GenreRepositoryTest {
    @Autowired
    private GenreRepository repository;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Genre expected = new Genre(1, "Philosophy");

        repository.update(expected.getName(), expected.getId());

        final Genre actual = repository.findById(1L).get();

        assertEquals(expected, actual);
    }
}
