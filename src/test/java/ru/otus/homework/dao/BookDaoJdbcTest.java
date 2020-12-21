package ru.otus.homework.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {
    @Autowired
    private BookDaoJdbc jdbc;
    private final Book expectedUlysses = new Book(1, "Ulysses", "James Joyce", "Modernist novel");

    @Test
    void testInsertionByComparing() {
        final Book expected = new Book(2, "Discipline and Punish", "Michel Foucault", "Philosophy");
        jdbc.insert(expected);
        final Book actual = jdbc.getBookById(2);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectBookById() {
        final Book actual = jdbc.getBookById(1);

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByTitle() {
        final Book actual = jdbc.getBookByTitle("Ulysses");

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByAuthorId() {
        final Book actual = jdbc.getBookByAuthor("James Joyce");

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByGenreId() {
        final Book actual = jdbc.getBookByGenre("Modernist novel");

        assertEquals(expectedUlysses, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Book expected = new Book(1, "Discipline and Punish", "Michel Foucault", "Philosophy");
        jdbc.update(expected);
        final Book actual = jdbc.getBookById(1);

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteBookById() {
        jdbc.deleteById(1);
        assertThrows(EmptyResultDataAccessException.class, () -> jdbc.getBookById(1));
    }

    @Test
    void shouldReturnCorrectListOfBooks(){
        final List<Book> expected = List.of(expectedUlysses);
        final List<Book> actual = jdbc.getAll();

        assertEquals(expected, actual);
    }
}
