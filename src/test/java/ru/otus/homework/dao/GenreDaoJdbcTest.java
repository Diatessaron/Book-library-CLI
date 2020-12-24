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
@Import({GenreDaoJdbc.class, BookDaoJdbc.class})
class GenreDaoJdbcTest {
    @Autowired
    private GenreDaoJdbc jdbc;
    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    @Test
    void testCountMethod(){
        final int expected = 1;
        final int actual = jdbc.count();

        assertEquals(expected, actual);
    }

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
    void shouldReturnCorrectGenreByName() {
        final Genre expected = new Genre(1, "Modernist novel");
        final Genre actual = jdbc.getGenreByName("Modernist novel");

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetGenreByNameMethodInvocation(){
        assertThrows(EmptyResultDataAccessException.class, () -> jdbc.getGenreByName("genre"));
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
        bookDaoJdbc.deleteById(1);
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
