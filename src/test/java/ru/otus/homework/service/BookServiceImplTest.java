package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceImplTest {
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private BookServiceImpl service;

    private final Book expectedUlysses = new Book("Ulysses", new Author("James Joyce"),
            new Genre("Modernist novel"));

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSaveBookMethodWithParameters() {
        final Author author = new Author("Michel Foucault");
        final Genre genre = new Genre("Philosophy");
        final Book book = new Book("Discipline and Punish", author, genre);

        when(authorRepository.findByName(author.getName())).thenReturn(List.of(author));
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookRepository.findByTitle(book.getTitle())).thenReturn(List.of(book));

        service.saveBook("Discipline and Punish", "Michel Foucault",
                "Philosophy");

        final Book actualBook = service.getBookByTitle("Discipline and Punish");
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("Discipline and Punish"))
                .matches(s -> s.getAuthor().getName().equals("Michel Foucault"))
                .matches(s -> s.getGenre().getName().equals("Philosophy"));

        final InOrder inOrder = inOrder(bookRepository, authorRepository, genreRepository);
        inOrder.verify(authorRepository).findByName(author.getName());
        inOrder.verify(genreRepository).findByName(genre.getName());
        inOrder.verify(bookRepository).save(book);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSaveBookMethodWithOldAuthorAndGenre() {
        final Author author = new Author("James Joyce");
        final Genre genre = new Genre("Modernist novel");
        final Book book = new Book("A Portrait of the Artist as a Young Man", author, genre);

        when(authorRepository.findByName(author.getName())).thenReturn(List.of(author));
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookRepository.findByTitle(book.getTitle())).thenReturn(List.of(book));

        service.saveBook("A Portrait of the Artist as a Young Man",
                "James Joyce", "Modernist novel");

        final Book actualBook = service.getBookByTitle("A Portrait of the Artist as a Young Man");
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("A Portrait of the Artist as a Young Man"))
                .matches(s -> s.getAuthor().getName().equals("James Joyce"))
                .matches(s -> s.getGenre().getName().equals("Modernist novel"));

        final InOrder inOrder = inOrder(bookRepository, authorRepository, genreRepository);
        inOrder.verify(authorRepository).findByName(author.getName());
        inOrder.verify(genreRepository).findByName(genre.getName());
        inOrder.verify(bookRepository).save(book);
    }

    @Test
    void shouldReturnCorrectBookByTitle() {
        final Author author = new Author("James Joyce");
        final Genre genre = new Genre("Modernist novel");
        final Book book = new Book("Ulysses", author, genre);

        when(bookRepository.findByTitle(book.getTitle())).thenReturn(List.of(book));

        final Book actual = service.getBookByTitle(expectedUlysses.getTitle());

        assertEquals(expectedUlysses, actual);

        verify(bookRepository, times(1)).findByTitle(book.getTitle());
    }

    @Test
    void shouldReturnCorrectBookByGenre() {
        final Author author = new Author("James Joyce");
        final Genre genre = new Genre("Modernist novel");
        final Book book = new Book("Ulysses", author, genre);

        when(bookRepository.findByGenre_Name(book.getGenre().getName())).thenReturn(List.of(book));

        final Book actual = service.getBookByGenre(expectedUlysses.getGenre().getName());

        assertEquals(expectedUlysses, actual);

        verify(bookRepository, times(1)).findByGenre_Name(book.getGenre().getName());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectListOfBooks() {
        final Author author = new Author("Michel Foucault");
        final Genre genre = new Genre("Philosophy");
        final Book book = new Book("Discipline And Punish", author, genre);
        final List<Book> expected = List.of(expectedUlysses, book);

        when(bookRepository.findAll()).thenReturn(List.of(expectedUlysses, book));

        final List<Book> actual = service.getAll();

        assertThat(actual).isNotNull().matches(a -> a.size() == expected.size());

        verify(bookRepository, times(1)).findAll();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testUpdateBookMethodWithParameters() {
        final Author author = new Author("Michel Foucault");
        final Genre genre = new Genre("Philosophy");
        final Book book = new Book("Ulysses", author, genre);

        when(authorRepository.findByName(author.getName())).thenReturn(List.of(author));
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        when(bookRepository.save(new Book("Discipline and Punish", author, genre))).thenReturn(book);
        when(bookRepository.findByTitle(book.getTitle())).thenReturn(List.of(book));
        when(commentRepository.findByBook_Title(book.getTitle())).thenReturn(List.of());
        when(bookRepository.findByTitle("Discipline and Punish")).thenReturn
                (List.of(new Book("Discipline and Punish", author, genre)));

        service.updateBook("Ulysses", "Discipline and Punish", "Michel Foucault",
                "Philosophy");

        final Book actualBook = service.getBookByTitle("Discipline and Punish");
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("Discipline and Punish"))
                .matches(s -> s.getAuthor().getName().equals("Michel Foucault"))
                .matches(s -> s.getGenre().getName().equals("Philosophy"));

        final InOrder inOrder = inOrder(bookRepository, authorRepository, genreRepository, commentRepository);
        inOrder.verify(authorRepository).findByName(author.getName());
        inOrder.verify(genreRepository).findByName(genre.getName());
        inOrder.verify(bookRepository).findByTitle("Ulysses");
        inOrder.verify(bookRepository).save(book);
        inOrder.verify(commentRepository).findByBook_Title(expectedUlysses.getTitle());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateBookMethodWithOldAuthorAndGenre() {
        final Author author = new Author("James Joyce");
        final Genre genre = new Genre("Modernist novel");
        final Book book = new Book("Ulysses", author, genre);

        when(authorRepository.findByName(author.getName())).thenReturn(List.of(author));
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        when(bookRepository.save(new Book("Discipline and Punish", author, genre))).thenReturn(book);
        when(bookRepository.findByTitle(book.getTitle())).thenReturn(List.of(book));
        when(commentRepository.findByBook_Title(book.getTitle())).thenReturn(List.of());
        when(bookRepository.findByTitle("A Portrait of the Artist as a Young Man")).thenReturn
                (List.of(new Book("A Portrait of the Artist as a Young Man", author, genre)));

        service.updateBook("Ulysses", "A Portrait of the Artist as a Young Man",
                "James Joyce", "Modernist novel");

        final Book actualBook = service.getBookByTitle("A Portrait of the Artist as a Young Man");
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("A Portrait of the Artist as a Young Man"))
                .matches(s -> s.getAuthor().getName().equals("James Joyce"))
                .matches(s -> s.getGenre().getName().equals("Modernist novel"));

        final InOrder inOrder = inOrder(bookRepository, authorRepository, genreRepository, commentRepository);
        inOrder.verify(authorRepository).findByName(author.getName());
        inOrder.verify(genreRepository).findByName(genre.getName());
        inOrder.verify(bookRepository).findByTitle("Ulysses");
        inOrder.verify(bookRepository).save(book);
        inOrder.verify(commentRepository).findByBook_Title(expectedUlysses.getTitle());
    }
}
