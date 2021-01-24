package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.BookRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Import(CommentServiceImpl.class)
class CommentServiceImplTest {
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private BookRepository bookRepository;

    private final Book ulysses = new Book("Ulysses", new Author("James Joyce"),
            new Genre("Modernist novel"));
    private final Comment ulyssesComment = new Comment("Published in 1922", ulysses.getTitle());

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSaveByComparing() {
        final Author foucault = new Author("Michel Foucault");
        final Genre philosophy = new Genre("Philosophy");
        final Book book = new Book("Discipline and Punish", foucault, philosophy);
        final Comment expected = new Comment("Published in 1975", book.getTitle());

        bookRepository.save(book);
        commentService.saveComment(expected.getBook().getTitle(), expected.getContent());
        final Comment actual = commentService.getCommentByContent(expected.getContent());

        assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    void shouldReturnCorrectCommentByContent() {
        final Comment actual = commentService.getCommentByContent(ulyssesComment.getContent());

        assertEquals(ulyssesComment, actual);
    }

    @Test
    void testGetCommentByBookMethod(){
        final List<Comment> expected = List.of(ulyssesComment);
        final List<Comment> actual = commentService.getCommentsByBook("Ulysses");

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectListOfComments(){
        final Author foucault = new Author("Michel Foucault");
        final Genre philosophy = new Genre("Philosophy");
        final Book book = new Book("Discipline and Punish", foucault, philosophy);

        final Comment disciplineAndPunishComment = new Comment("Published in 1975", book.getTitle());
        final List<Comment> expected = List.of(this.ulyssesComment, disciplineAndPunishComment);

        bookRepository.save(book);
        commentService.saveComment(disciplineAndPunishComment.getBook().getTitle(),
                disciplineAndPunishComment.getContent());
        final List<Comment> actual = commentService.getAll();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateCommentCorrectly() {
        commentService.updateComment("Published in 1922", "Comment");

        final Comment actualComment = commentService.getCommentByContent("Comment");
        assertThat(actualComment).isNotNull().matches(s -> !s.getContent().isBlank())
                .matches(s -> s.getContent().equals("Comment"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testDeleteByIdMethodByResultStringComparing(){
        final String expected = "Ulysses comment was deleted";
        final String actual = commentService.deleteByContent("Published in 1922");

        assertEquals(expected, actual);
    }
}
