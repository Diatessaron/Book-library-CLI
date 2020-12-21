package ru.otus.homework.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {
    @Autowired
    private GenreDaoJdbc jdbc;

    @Test
    void testInsertionByComparing() {
        final Genre expected = new Genre(2, "Philosophy");
        jdbc.insert(expected);
        final Genre actual = jdbc.getGenreById(2);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectGenreById() {
        final Genre expected = new Genre(1, "Modernist novel");
        final Genre actual = jdbc.getGenreById(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectGenreByTitle() {
        final Genre expected = new Genre(1, "Modernist novel");
        final Genre actual = jdbc.getGenreByTitle("Modernist novel");

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Genre expected = new Genre(1, "Modernist novel");
        jdbc.update(expected);
        final Genre actual = jdbc.getGenreById(1);

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteGenreById() {
        jdbc.deleteById(1);
        assertThrows(EmptyResultDataAccessException.class, () -> jdbc.getGenreById(1));
    }

    @Test
    void shouldReturnCorrectListOfAuthors(){
        final List<Genre> expected = List.of(new Genre(1, "Modernist novel"));
        final List<Genre> actual = jdbc.getAll();

        assertEquals(expected, actual);
    }
}
