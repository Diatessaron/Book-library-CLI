package ru.otus.homework.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BookRepositoryImpl.class)
class BookRepositoryImplTest {
    @Autowired
    private BookRepositoryImpl repository;
    @Autowired
    private TestEntityManager em;

    private final Book expectedUlysses = new Book(1, "Ulysses", new Author(1, "James Joyce"),
            new Genre(1, "Modernist novel"));

    {
        expectedUlysses.getComments().add(new Comment(1L, "Published in 1922"));
    }

    @Test
    void testSaveByComparing() {
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");

        final Book expected = new Book(0, "Discipline and Punish", foucault,
                philosophy);
        repository.save(expected);
        final Book actual = repository.getBookById(expected.getId()).orElseThrow
                (() -> new IllegalArgumentException("Incorrect id"));

        assertEquals(expected, actual);
    }

    @Test
    void shouldHavePositiveId(){
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline and Punish", foucault, philosophy);
        repository.save(book);

        assertThat(book.getId()).isPositive();
    }

    @Test
    void shouldHaveCorrectData(){
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline and Punish", foucault, philosophy);
        repository.save(book);
        Book actualBook = em.find(Book.class, book.getId());

        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""));
    }

    @Test
    void shouldReturnCorrectBookById() {
        final Book actual = repository.getBookById(1L).orElseThrow
                (() -> new IllegalArgumentException("Book with 1 id couldn't be found "));

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByTitle() {
        final Book actual = repository.getBookByTitle(expectedUlysses.getTitle());

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByAuthor() {
        final Book actual = repository.getBookByAuthor(expectedUlysses.getAuthor().getName());

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByGenre() {
        final Book actual = repository.getBookByGenre(expectedUlysses.getGenre().getName());

        assertEquals(expectedUlysses, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectBookByComment(){
        final Comment comment = new Comment(0L, "Published in 1922");
        expectedUlysses.getComments().add(comment);
        em.merge(expectedUlysses);

        final Book actual = repository.getBookByComment(expectedUlysses.getComments().get(0).getContent());

        assertThat(actual).isNotNull().matches(b -> b.getId()==expectedUlysses.getId())
                .matches(b -> !b.getTitle().equals(""))
                .matches(b -> b.getTitle().equals(expectedUlysses.getTitle()))
                .matches(b -> b.getAuthor().equals(expectedUlysses.getAuthor()))
                .matches(b -> b.getGenre().equals(expectedUlysses.getGenre()))
                .matches(b -> b.getComments().get(0).equals(expectedUlysses.getComments().get(0)));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectListOfBooks() {
        final Author author = new Author(0, "Michel Foucault");
        final Genre genre = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline And Punish", author, genre);
        final List<Book> expected = List.of(expectedUlysses, book);
        repository.save(book);
        final List<Book> actual = repository.getAll();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Author author = new Author(0L, "Michel Foucault");
        final Genre genre = new Genre(0L, "Philosophy");
        final Book expected = new Book(1L, "Discipline And Punish", author, genre);
        expected.getComments().add(new Comment(1L, "New comment"));
        repository.update(expected);
        final Book actual = repository.getBookById(1L).get();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteBookById() {
        repository.deleteById(1);
        assertTrue(repository.getBookById(1).isEmpty());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void commentShouldBeDeletedBeforeBookDeletion(){
        repository.deleteById(1L);
        assertNull(em.find(Comment.class, 1L));
    }

    @Test
    void shouldReturnCorrectStudentsListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        final List<Book> books = repository.getAll();
        assertThat(books).isNotNull().hasSize(1)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenre() != null)
                .allMatch(b -> b.getComments() != null)
                .allMatch(b -> b.getComments().size()>0);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(4L);
    }
}
