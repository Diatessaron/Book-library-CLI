package ru.otus.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceImplTest {
    @Autowired
    private BookServiceImpl service;

    private final Book expectedUlysses = new Book("Ulysses", new Author("James Joyce"),
            new Genre("Modernist novel"));

    @BeforeEach
    void setUp(){
        service.saveBook(expectedUlysses.getTitle(), expectedUlysses.getAuthor().getName(),
                expectedUlysses.getGenre().getName());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testSaveBookMethodWithParameters() {
        service.saveBook("Discipline and Punish", "Michel Foucault",
                "Philosophy");

        final Book actualBook = service.getBookByTitle("Discipline and Punish");
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("Discipline and Punish"))
                .matches(s -> s.getAuthor().getName().equals("Michel Foucault"))
                .matches(s -> s.getGenre().getName().equals("Philosophy"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testSaveBookMethodWithOldAuthorAndGenre() {
        service.saveBook("A Portrait of the Artist as a Young Man",
                "James Joyce", "Modernist novel");

        final Book actualBook = service.getBookByTitle("A Portrait of the Artist as a Young Man");
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("A Portrait of the Artist as a Young Man"))
                .matches(s -> s.getAuthor().getName().equals("James Joyce"))
                .matches(s -> s.getGenre().getName().equals("Modernist novel"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectBookByTitle() {
        final Book actual = service.getBookByTitle(expectedUlysses.getTitle());

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByGenre() {
        final Book actual = service.getBookByGenre(expectedUlysses.getGenre().getName());

        assertEquals(expectedUlysses, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectListOfBooks() {
        final Author author = new Author("Michel Foucault");
        final Genre genre = new Genre("Philosophy");
        final Book book = new Book("Discipline And Punish", author, genre);
        final List<Book> expected = List.of(expectedUlysses, book);

        service.saveBook("Discipline And Punish", "Michel Foucault",
                "Philosophy");
        final List<Book> actual = service.getAll();

        assertThat(actual).isNotNull().matches(a -> a.size() == expected.size());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testUpdateBookMethodWithParameters() {
        service.updateBook("Ulysses", "Discipline and Punish", "Michel Foucault",
                "Philosophy");

        final Book actualBook = service.getBookByTitle("Discipline and Punish");
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("Discipline and Punish"))
                .matches(s -> s.getAuthor().getName().equals("Michel Foucault"))
                .matches(s -> s.getGenre().getName().equals("Philosophy"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testUpdateBookMethodWithOldAuthorAndGenre() {
        service.updateBook("Ulysses", "A Portrait of the Artist as a Young Man",
                "James Joyce", "Modernist novel");

        final Book actualBook = service.getBookByTitle("A Portrait of the Artist as a Young Man");
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("A Portrait of the Artist as a Young Man"))
                .matches(s -> s.getAuthor().getName().equals("James Joyce"))
                .matches(s -> s.getGenre().getName().equals("Modernist novel"));
    }
}
