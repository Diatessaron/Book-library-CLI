package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.dao.BookDaoJdbc;
import ru.otus.homework.dao.GenreDaoJdbc;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GenreCommandsTest {
    @MockBean
    private GenreDaoJdbc genreDaoJdbc;
    @MockBean
    private BookDaoJdbc bookDaoJdbc;

    @Autowired
    private Shell shell;
    private final Genre novel = new Genre(1, "Modernist novel");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertMethodByTimesOfDaoInvocation() {
        shell.evaluate(() -> "gInsert 2 philosophy");

        verify(genreDaoJdbc, times(1)).insert(new Genre(2, "philosophy"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessage(){
        final String expected = "You successfully inserted a philosophy to repository";
        final String actual = shell.evaluate(() -> "gInsert 2 philosophy").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetGenreByIdByMessageComparison() {
        when(genreDaoJdbc.getGenreById(1)).thenReturn(novel);
        final String expected = novel.toString();
        final String actual = shell.evaluate(() -> "genreById 1").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAuthorByNameByMessageComparison() {
        when(genreDaoJdbc.getGenreByName(novel.getName())).thenReturn(novel);
        final String expected = novel.toString();
        final String actual = shell.evaluate(() -> "genreByTitle Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(genreDaoJdbc.getAll()).thenReturn(List.of(novel));
        final String expected = List.of(novel).toString();
        final String actual = shell.evaluate(() -> "gGetAll").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethod() {
        final String expected = "Modernist novel was updated";
        final String actual = shell.evaluate(() -> "gUpdate 1 Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(genreDaoJdbc.getGenreById(1)).thenReturn(novel);
        when(bookDaoJdbc.getBookByGenre(novel.getName())).thenReturn
                (new Book(1, "Ulysses", new Author(1, "James Joyce"), novel));

        final String expected = "Modernist novel was deleted";
        final String actual = shell.evaluate(() -> "gDelete 1").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void bookShouldBeDeletedBeforeGenreDeletion(){
        final String genreName = novel.getName();
        when(genreDaoJdbc.getGenreById(1)).thenReturn(novel);
        when(bookDaoJdbc.getBookByGenre(genreName)).thenReturn
                (new Book(1, "Ulysses", new Author(1, "James Joyce"), novel));
        shell.evaluate(() -> "gDelete 1");

        final long bookId = bookDaoJdbc.getBookByGenre(genreName).getId();

        final InOrder inOrder = inOrder(bookDaoJdbc, genreDaoJdbc);
        inOrder.verify(genreDaoJdbc).getGenreById(1);
        inOrder.verify(bookDaoJdbc).getBookByGenre(genreName);
        inOrder.verify(bookDaoJdbc).deleteById(bookId);
        inOrder.verify(genreDaoJdbc).deleteById(1);
    }
}
