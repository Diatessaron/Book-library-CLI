package ru.otus.homework.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentCommandsTest {
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private Shell shell;
    private final Book ulysses = new Book("Ulysses", new Author("James Joyce"),
            new Genre("Modernist novel"));
    private final Comment comment = new Comment("Published in 1922", ulysses);

    @BeforeEach
    void setUp(){
        bookRepository.save(ulysses);
        commentRepository.save(comment);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertMethodByTimesOfRepositoryInvocation() {
        when(bookRepository.findByTitle(ulysses.getTitle())).thenReturn(Optional.of(ulysses));

        shell.evaluate(() -> "ci Ulysses Second,comment");

        verify(commentRepository, times(1)).save
                (new Comment("Second comment", ulysses));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterInsertMethodInvocation() {
        final Genre genre = new Genre("genre");
        final Author author = new Author("author");
        final Book book = new Book("book", author, genre);

        when(bookRepository.findByTitle("Ulysses")).thenReturn(Optional.of(book));

        final String expected = "You successfully added a comment to book";
        final String actual = shell.evaluate(() -> "cInsert Ulysses Second,comment,to,Ulysses").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetCommentByContentByMessageComparison() {
        when(commentRepository.findByContent(comment.getContent()))
                .thenReturn(Optional.of(comment));

        final String expected = comment.toString();
        final String actual = shell.evaluate(() -> "cByContent Published,in,1922").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        final String expected = List.of(comment).toString();
        final String actual = shell.evaluate(() -> "cGetAll").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethod() {
        when(commentRepository.findBookByComment("Published in 1922")).thenReturn(Optional.of(comment));
        when(commentRepository.findByContent("Published in 1922")).thenReturn(Optional.of(comment));

        final String expected = "Ulysses comment was updated";
        final String actual = shell.evaluate(() -> "cUpdate Published,in,1922 Good,book").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(commentRepository.findByContent("Published in 1922")).thenReturn(Optional.of(comment));
        when(commentRepository.findBookByComment(comment.getContent()))
                .thenReturn(Optional.of(comment));

        final String expected = "Ulysses comment was deleted";
        final String actual = shell.evaluate(() -> "cDelete Published,in,1922").toString();

        assertEquals(expected, actual);
    }
}
