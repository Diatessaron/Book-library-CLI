package ru.otus.homework.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

import javax.persistence.NoResultException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(CommentRepositoryImpl.class)
class CommentRepositoryImplTest {
    @Autowired
    private CommentRepositoryImpl repository;
    @Autowired
    private TestEntityManager em;

    private Comment ulyssesComment = new Comment(1L, "Published in 1922");

    @Test
    void testCountMethod() {
        final long expected = 1L;
        final long actual = repository.count();

        assertEquals(expected, actual);
    }

    @Test
    void testSaveByComparing() {
        final Comment expected = new Comment(0L, "Published in 1975");
        repository.save(expected);
        final Comment actual = repository.getCommentById(2L).orElseThrow(() ->
                new IllegalArgumentException("Incorrect id"));

        assertEquals(expected, actual);
    }

    @Test
    void shouldHavePositiveId() {
        final Comment comment = new Comment(0L, "Published in 1975");
        repository.save(comment);

        assertThat(comment.getId()).isPositive();
    }

    @Test
    void shouldHaveCorrectData(){
        final Comment comment = new Comment(0L, "Published in 1975");
        repository.save(comment);
        final Comment actualComment = em.find(Comment.class, comment.getId());

        assertThat(actualComment).isNotNull().matches(s -> !s.getContent().equals(""))
                .matches(s -> s.getContent().equals("Published in 1975"))
                .matches(s -> s.getId()==2L);
    }

    @Test
    void shouldReturnCorrectCommentById() {
        final Comment actual = repository.getCommentById(1L).orElseThrow(() ->
                new IllegalArgumentException("Incorrect id"));

        assertEquals(ulyssesComment, actual);
    }

    @Test
    void shouldReturnCorrectCommentByName() {
        final Comment actual = repository.getCommentByContent(ulyssesComment.getContent());

        assertEquals(ulyssesComment, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetCommentByNameMethodInvocation(){
        assertThrows(NoResultException.class, () -> repository.getCommentByContent("comment"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectListOfComments(){
        final Comment disciplineAndPunishComment = new Comment(2L, "Published in 1975");
        final List<Comment> expected = List.of(this.ulyssesComment, disciplineAndPunishComment);

        repository.save(disciplineAndPunishComment);
        final List<Comment> actual = repository.getAll();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Comment expected = new Comment(1L, "Published in 1975");
        repository.update(expected);
        final Comment actual = repository.getCommentById(1L).orElseThrow(() ->
                new IllegalArgumentException("Incorrect id"));

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteCommentById() {
        repository.deleteById(1);
        assertTrue(repository.getCommentById(1L).isEmpty());
    }
}
