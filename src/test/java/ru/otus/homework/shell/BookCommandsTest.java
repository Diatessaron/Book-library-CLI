package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.AuthorRepositoryImpl;
import ru.otus.homework.repository.BookRepositoryImpl;
import ru.otus.homework.repository.GenreRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookCommandsTest {
    @MockBean
    private BookRepositoryImpl bookRepository;
    @MockBean
    private AuthorRepositoryImpl authorRepository;
    @MockBean
    private GenreRepositoryImpl genreRepository;

    @Autowired
    private Shell shell;
    private final Book ulysses = new Book(1, "Ulysses", new Author(1, "James Joyce"),
            new Genre(1, "Modernist novel"));

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertMethodByTimesOfRepositoryInvocation() {
        final Author author = new Author(0L, "author");
        final Genre genre = new Genre(0L, "genre");
        final Book book = new Book(0L, "book", author, genre);
        when(authorRepository.getAuthorByName("author")).thenReturn(author);
        when(genreRepository.getGenreByName("genre")).thenReturn(genre);
        shell.evaluate(() -> "bInsert book author genre");

        verify(authorRepository, times(1)).getAuthorByName("author");
        verify(genreRepository, times(1)).getGenreByName("genre");
        verify(bookRepository, times(1)).save(book);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterInsertMethod(){
        when(authorRepository.getAuthorByName("author")).thenReturn(new Author(0L, "author"));
        when(genreRepository.getGenreByName("genre")).thenReturn(new Genre(0L, "genre"));
        final String expected = "You successfully inserted a book to repository";
        final String actual = shell.evaluate(() -> "bInsert book author genre").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageWithMultipleArgumentsAfterInsertMethod(){
        when(authorRepository.getAuthorByName("author")).thenReturn(new Author(0L, "author"));
        when(genreRepository.getGenreByName("genre")).thenReturn(new Genre(0L, "genre"));
        final String expected = "You successfully inserted a Discipline and Punish to repository";
        final String actual = shell.evaluate(() -> "bInsert Discipline,and,Punish Michel,Foucault Philosophy").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterInsertMethodWithOldAuthorAndGenre(){
        final String expected = "You successfully inserted a A Portrait of the Artist as a Young Man " +
                "to repository";
        final String actual = shell.evaluate(() -> "bInsert A,Portrait,of,the,Artist,as,a,Young,Man " +
                "James,Joyce Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByIdByMessageComparison() {
        when(bookRepository.getBookById(1L)).thenReturn(Optional.of(ulysses));
        final String expected = Optional.of(ulysses).toString();
        final String actual = shell.evaluate(() -> "bookById 1").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByTitleByMessageComparison() {
        when(bookRepository.getBookByTitle(ulysses.getTitle())).thenReturn(ulysses);
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookByTitle Ulysses").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByAuthorByMessageComparison() {
        when(bookRepository.getBookByAuthor(ulysses.getAuthor().getName())).thenReturn(ulysses);
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookByAuthor James,Joyce").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByGenreByMessageComparison() {
        when(bookRepository.getBookByGenre(ulysses.getGenre().getName())).thenReturn(ulysses);
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookByGenre Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(bookRepository.getAll()).thenReturn(List.of(ulysses));
        final String expected = List.of(ulysses).toString();
        final String actual = shell.evaluate(() -> "bGetAll").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageWithMultipleArgumentsAfterUpdateMethod() {
        when(authorRepository.getAuthorByName("Michel Foucault")).thenReturn(new Author(0L, "Michel Foucault"));
        when(genreRepository.getGenreByName("Philosophy")).thenReturn(new Genre(0L, "Philosophy"));
        final String expected = "Discipline and Punish was updated";
        final String actual = shell.evaluate(() -> "bUpdate 1 Discipline,and,Punish Michel,Foucault Philosophy").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethodWithOldAuthorAndGenre(){
        when(authorRepository.getAuthorByName("Michel Foucault")).thenReturn(new Author(0L, "Michel Foucault"));
        when(genreRepository.getGenreByName("Philosophy")).thenReturn(new Genre(0L, "Philosophy"));
        final String expected = "A Portrait of the Artist as a Young Man was updated";
        final String actual = shell.evaluate(() -> "bUpdate 1 A,Portrait,of,the,Artist,as,a,Young,Man " +
                "James,Joyce Modern,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(bookRepository.getBookById(1L)).thenReturn(Optional.of(ulysses));
        final String expected = "Ulysses was deleted";
        final String actual = shell.evaluate(() -> "bDelete 1").toString();

        assertEquals(expected, actual);
    }
}
