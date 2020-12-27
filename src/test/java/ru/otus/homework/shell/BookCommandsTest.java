package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.dao.BookDaoJdbc;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookCommandsTest {
    @MockBean
    private BookDaoJdbc bookDaoJdbc;

    @Autowired
    private Shell shell;
    private final Book ulysses = new Book(1, "Ulysses", new Author(1, "James Joyce"),
            new Genre(1, "Modernist novel"));

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertMethodByTimesOfDaoInvocation() {
        shell.evaluate(() -> "bInsert 2 book author genre");

        verify(bookDaoJdbc, times(1)).insert(new Book(2, "book", new Author(2, "author"), new Genre(2, "genre")));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterInsertMethod(){
        final String expected = "You successfully inserted a book to repository";
        final String actual = shell.evaluate(() -> "bInsert 2 book author genre").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageWithMultipleArgumentsAfterInsertMethod(){
        final String expected = "You successfully inserted a Discipline and Punish to repository";
        final String actual = shell.evaluate(() -> "bInsert 2 Discipline,and,Punish Michel,Foucault Philosophy").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterInsertMethodWithOldAuthorAndGenre(){
        final String expected = "You successfully inserted a A Portrait of the Artist as a Young Man to repository";
        final String actual = shell.evaluate(() -> "bInsert 2 A,Portrait,of,the,Artist,as,a,Young,Man James,Joyce Modern,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByIdByMessageComparison() {
        when(bookDaoJdbc.getBookById(1)).thenReturn(ulysses);
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookById 1").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByTitleByMessageComparison() {
        when(bookDaoJdbc.getBookByTitle(ulysses.getTitle())).thenReturn(ulysses);
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookByTitle Ulysses").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByAuthorByMessageComparison() {
        when(bookDaoJdbc.getBookByAuthor(ulysses.getAuthor().getName())).thenReturn(ulysses);
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookByAuthor James,Joyce").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByGenreByMessageComparison() {
        when(bookDaoJdbc.getBookByGenre(ulysses.getGenre().getName())).thenReturn(ulysses);
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookByGenre Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(bookDaoJdbc.getAll()).thenReturn(List.of(ulysses));
        final String expected = List.of(ulysses).toString();
        final String actual = shell.evaluate(() -> "bGetAll").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageWithMultipleArgumentsAfterUpdateMethod() {
        final String expected = "Discipline and Punish was updated";
        final String actual = shell.evaluate(() -> "bUpdate 1 Discipline,and,Punish Michel,Foucault Philosophy").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethodWithOldAuthorAndGenre(){
        final String expected = "A Portrait of the Artist as a Young Man was updated";
        final String actual = shell.evaluate(() -> "bUpdate 1 A,Portrait,of,the,Artist,as,a,Young,Man James,Joyce Modern,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(bookDaoJdbc.getBookById(1)).thenReturn(ulysses);
        final String expected = "Ulysses was deleted";
        final String actual = shell.evaluate(() -> "bDelete 1").toString();

        assertEquals(expected, actual);
    }
}