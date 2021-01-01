package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.BookRepositoryImpl;
import ru.otus.homework.repository.CommentRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentCommandsTest {
    @MockBean
    private CommentRepositoryImpl commentRepository;
    @MockBean
    private BookRepositoryImpl bookRepository;

    @Autowired
    private Shell shell;
    private final Comment comment = new Comment(1L, "Published in 1922");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertMethodByTimesOfRepositoryInvocation() {
        shell.evaluate(() -> "ci 1 Second,comment");

        verify(commentRepository, times(1)).save
                (new Comment(0L, "Second comment"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterInsertMethodInvocation(){
        final Genre genre = new Genre(1L, "genre");
        final Author author = new Author(1L, "author");
        final Book book = new Book(1L, "book", author, genre);
        when(bookRepository.getBookById(1L)).thenReturn(Optional.of(book));
        final String expected = "You successfully added a comment to book";
        final String actual = shell.evaluate(() -> "cInsert 1 Second,comment,to,Ulysses").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetCommentByIdByMessageComparison() {
        when(commentRepository.getCommentById(1L)).thenReturn(Optional.of(comment));
        final String expected = Optional.of(comment).get().toString();
        final String actual = shell.evaluate(() -> "commentById 1").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetCommentByContentByMessageComparison() {
        when(commentRepository.getCommentByContent(comment.getContent())).thenReturn(comment);
        final String expected = comment.toString();
        final String actual = shell.evaluate(() -> "cByContent Published,in,1922").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(commentRepository.getAll()).thenReturn(List.of(comment));
        final String expected = List.of(comment).toString();
        final String actual = shell.evaluate(() -> "cGetAll").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethod() {
        final Genre genre = new Genre(1L, "genre");
        final Author author = new Author(1L, "author");
        final Book book = new Book(1L, "Book", author, genre);
        book.getComments().add(comment);

        when(bookRepository.getBookById(1L)).thenReturn(Optional.of(book));

        final String expected = "Book comment was updated";
        final String actual = shell.evaluate(() -> "cUpdate 1 1 Good,book").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        final Genre genre = new Genre(1L, "genre");
        final Author author = new Author(1L, "author");
        final Book book = new Book(1L, "Book", author, genre);
        book.getComments().add(comment);

        when(commentRepository.getCommentById(1L)).thenReturn(Optional.of(comment));
        when(bookRepository.getBookByComment(comment.getContent())).thenReturn(book);

        final String expected = "Book comment was deleted";
        final String actual = shell.evaluate(() -> "cDelete 1").toString();

        assertEquals(expected, actual);
    }
}
