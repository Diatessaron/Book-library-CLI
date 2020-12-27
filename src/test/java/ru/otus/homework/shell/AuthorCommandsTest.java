package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.dao.AuthorDaoJdbc;
import ru.otus.homework.dao.BookDaoJdbc;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorCommandsTest {
    @MockBean
    private AuthorDaoJdbc authorDaoJdbc;
    @MockBean
    private BookDaoJdbc bookDaoJdbc;

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

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(authorDaoJdbc.getAuthorById(1)).thenReturn(jamesJoyce);
        when(bookDaoJdbc.getBookByAuthor(jamesJoyce.getName())).thenReturn
                (new Book(1, "Ulysses", jamesJoyce, new Genre(1, "Modernist novel")));

        final String expected = "James Joyce was deleted";
        final String actual = shell.evaluate(() -> "aDelete 1").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void bookShouldBeDeletedBeforeAuthorDeletion(){
        final String authorName = jamesJoyce.getName();
        when(authorDaoJdbc.getAuthorById(1)).thenReturn(jamesJoyce);
        when(bookDaoJdbc.getBookByAuthor(authorName)).thenReturn
                (new Book(1, "Ulysses", jamesJoyce, new Genre(1, "Modernist novel")));
        shell.evaluate(() -> "aDelete 1");

        final long bookId = bookDaoJdbc.getBookByAuthor(authorName).getId();

        final InOrder inOrder = inOrder(bookDaoJdbc, authorDaoJdbc);
        inOrder.verify(authorDaoJdbc).getAuthorById(1);
        inOrder.verify(bookDaoJdbc).getBookByAuthor(authorName);
        inOrder.verify(bookDaoJdbc).deleteById(bookId);
        inOrder.verify(authorDaoJdbc).deleteById(1);
    }
}
