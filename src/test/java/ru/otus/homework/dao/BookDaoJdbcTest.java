package ru.otus.homework.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
class BookDaoJdbcTest {
    @Autowired
    private BookDaoJdbc jdbc;
    @Autowired
    private AuthorDaoJdbc authorDaoJdbc;
    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    private final Book expectedUlysses = new Book(1, "Ulysses", new Author(1, "James Joyce"),
            new Genre(1, "Modernist novel"));

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertionByComparing() {
        final Author foucault = new Author(2, "Michel Foucault");
        final Genre philosophy = new Genre(2, "Philosophy");
        authorDaoJdbc.insert(foucault);
        genreDaoJdbc.insert(philosophy);

        final Book expected = new Book(2, "Discipline and Punish", foucault,
                philosophy);
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
        final Author foucault = new Author(2, "Michel Foucault");
        final Genre philosophy = new Genre(2, "Philosophy");
        authorDaoJdbc.insert(foucault);
        genreDaoJdbc.insert(philosophy);

        final Book expected = new Book(1, "Discipline and Punish", foucault,
                philosophy);
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
