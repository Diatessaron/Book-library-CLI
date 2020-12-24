package ru.otus.homework.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class AuthorDaoJdbcTest {
    @Autowired
    private AuthorDaoJdbc jdbc;
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
        final Author expected = new Author(2, "Michel Foucault");
        jdbc.insert(expected);
        final Author actual = jdbc.getAuthorById(2);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectAuthorById() {
        final Author actual = jdbc.getAuthorById(1);
        final Author expected = new Author(1, "James Joyce");

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectAuthorByName() {
        final Author expected = new Author(1, "James Joyce");
        final Author actual = jdbc.getAuthorByName("James Joyce");

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetAuthorByNameMethodInvocation(){
        assertThrows(EmptyResultDataAccessException.class, () -> jdbc.getAuthorByName("author"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Author expected = new Author(1, "Michel Foucault");
        jdbc.update(expected);
        final Author actual = jdbc.getAuthorById(1);

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteAuthorById() {
        bookDaoJdbc.deleteById(1);
        jdbc.deleteById(1);
        assertThrows(EmptyResultDataAccessException.class, () -> jdbc.getAuthorById(1));
    }

    @Test
    void shouldReturnCorrectListOfAuthors(){
        final List<Author> expected = List.of(new Author(1, "James Joyce"),
                new Author(2, "Michel Foucault"));
        jdbc.insert(new Author(2, "Michel Foucault"));
        final List<Author> actual = jdbc.getAll();

        assertEquals(expected, actual);
    }
}
