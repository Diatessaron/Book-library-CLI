package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.dao.AuthorDaoJdbc;
import ru.otus.homework.domain.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorCommandsTest {
    @MockBean
    private AuthorDaoJdbc authorDaoJdbc;

    @Autowired
    private Shell shell;
    public final Author jamesJoyce = new Author(1, "James Joyce");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertMethodByTimesOfDaoInvocation() {
        shell.evaluate(() -> "aInsert 2 Michel,Foucault");

        verify(authorDaoJdbc, times(1)).insert(new Author(2, "Michel Foucault"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessage(){
        final String expected = "You successfully inserted a Michel Foucault to repository";
        final String actual = shell.evaluate(() -> "aInsert 2 Michel,Foucault").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAuthorByIdByMessageComparison() {
        when(authorDaoJdbc.getAuthorById(1)).thenReturn(jamesJoyce);
        final String expected = jamesJoyce.toString();
        final String actual = shell.evaluate(() -> "authorById 1").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAuthorByNameByMessageComparison() {
        when(authorDaoJdbc.getAuthorByName(jamesJoyce.getName())).thenReturn(jamesJoyce);
        final String expected = jamesJoyce.toString();
        final String actual = shell.evaluate(() -> "authorByName James,Joyce").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(authorDaoJdbc.getAll()).thenReturn(List.of(jamesJoyce));
        final String expected = List.of(jamesJoyce).toString();
        final String actual = shell.evaluate(() -> "aGetAll").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethod() {
        final String expected = "Michel Foucault was updated";
        final String actual = shell.evaluate(() -> "aUpdate 1 Michel,Foucault").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(authorDaoJdbc.getAuthorById(1)).thenReturn(jamesJoyce);
        final String expected = "James Joyce was deleted";
        final String actual = shell.evaluate(() -> "aDelete 1").toString();

        assertEquals(expected, actual);
    }
}
