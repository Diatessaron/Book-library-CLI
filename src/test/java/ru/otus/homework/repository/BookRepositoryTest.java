package ru.otus.homework.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository repository;
    @Autowired
    private TestEntityManager em;

    private final Book expectedUlysses = new Book(1, "Ulysses",
            new Author(1, "James Joyce"),
            new Genre(1, "Modernist novel"));

    @Test
    void shouldReturnCorrectBookByComment() {
        final Book actual = repository.findByComment_Content("Published in 1922")
                .orElseThrow(() -> new IllegalArgumentException("Incorrect comment"));

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectStudentsListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        final List<Book> books = repository.findAll();
        assertThat(books).isNotNull().hasSize(1)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenre() != null);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1L);
    }

    @Test
    void shouldReturnCorrectBookByTitleWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        final Book book = repository.findByTitle(expectedUlysses.getTitle()).get();
        assertThat(book).isNotNull().matches(b -> !b.getTitle().equals(""), "Blank title")
                .matches(b -> b.getId() == expectedUlysses.getId(), "Correct id")
                .matches(b -> b.getAuthor() != null, "Correct author")
                .matches(b -> b.getGenre() != null, "Correct genre");

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1L);
    }
}
