package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookCommandsTest {
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private Shell shell;
    private final Book ulysses = new Book("Ulysses", new Author("James Joyce"),
            new Genre("Modernist novel"));

    @Test
    void testInsertMethodByTimesOfRepositoryInvocation() {
        final Author author = new Author("author");
        final Genre genre = new Genre("genre");
        final Book book = new Book("book", author, genre);
        when(authorRepository.findByName("author")).thenReturn(Optional.of(author));
        when(genreRepository.findByName("genre")).thenReturn(Optional.of(genre));
        shell.evaluate(() -> "bInsert book author genre");

        verify(authorRepository, times(1)).findByName("author");
        verify(genreRepository, times(1)).findByName("genre");
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void shouldReturnCorrectMessageAfterInsertMethod() {
        when(authorRepository.findByName("author")).thenReturn(Optional.of
                (new Author("author")));
        when(genreRepository.findByName("genre")).thenReturn(Optional.of
                (new Genre("genre")));
        final String expected = "You successfully inserted a book to repository";
        final String actual = shell.evaluate(() -> "bInsert book author genre").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageWithMultipleArgumentsAfterInsertMethod() {
        when(authorRepository.findByName("author")).thenReturn(Optional.of
                (new Author("author")));
        when(genreRepository.findByName("genre")).thenReturn(Optional.of
                (new Genre("genre")));
        final String expected = "You successfully inserted a Discipline and Punish to repository";
        final String actual = shell.evaluate(() -> "bInsert Discipline,and,Punish Michel,Foucault Philosophy").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterInsertMethodWithOldAuthorAndGenre() {
        final String expected = "You successfully inserted a A Portrait of the Artist as a Young Man " +
                "to repository";
        final String actual = shell.evaluate(() -> "bInsert A,Portrait,of,the,Artist,as,a,Young,Man " +
                "James,Joyce Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByTitleByMessageComparison() {
        when(bookRepository.findByTitle(ulysses.getTitle())).thenReturn(List.of(ulysses));
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookByTitle Ulysses").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByAuthorByMessageComparison() {
        when(bookRepository.findByAuthor_Name(ulysses.getAuthor().getName())).thenReturn
                (List.of(ulysses));
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookByAuthor James,Joyce").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookByGenreByMessageComparison() {
        when(bookRepository.findByGenre_Name(ulysses.getGenre().getName())).thenReturn
                (List.of(ulysses));
        final String expected = ulysses.toString();
        final String actual = shell.evaluate(() -> "bookByGenre Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(bookRepository.findAll()).thenReturn(List.of(ulysses));
        final String expected = List.of(ulysses).toString();
        final String actual = shell.evaluate(() -> "bGetAll").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageWithMultipleArgumentsAfterUpdateMethod() {
        when(authorRepository.findByName("Michel Foucault")).thenReturn
                (Optional.of((new Author("Michel Foucault"))));
        when(genreRepository.findByName("Philosophy")).thenReturn
                (Optional.of(new Genre("Philosophy")));
        when(bookRepository.findByTitle("Ulysses")).thenReturn
                (List.of(ulysses));

        final String expected = "Discipline and Punish was updated";
        final String actual = shell.evaluate(() -> "bUpdate Ulysses Discipline,and,Punish Michel,Foucault Philosophy").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethodWithOldAuthorAndGenre() {
        when(authorRepository.findByName("Michel Foucault")).thenReturn
                (Optional.of(new Author("Michel Foucault")));
        when(genreRepository.findByName("Philosophy")).thenReturn
                (Optional.of(new Genre("Philosophy")));
        when(bookRepository.findByTitle("Ulysses")).thenReturn
                (List.of(ulysses));

        final String expected = "A Portrait of the Artist as a Young Man was updated";
        final String actual = shell.evaluate(() -> "bUpdate Ulysses A,Portrait,of,the,Artist,as,a,Young,Man " +
                "James,Joyce Modern,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(bookRepository.findByTitle("Ulysses")).thenReturn(List.of(ulysses));
        final String expected = "Ulysses was deleted";
        final String actual = shell.evaluate(() -> "bDelete Ulysses").toString();

        assertEquals(expected, actual);
    }
}
