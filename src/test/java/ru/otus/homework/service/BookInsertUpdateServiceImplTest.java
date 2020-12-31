package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.AuthorRepositoryImpl;
import ru.otus.homework.repository.BookRepositoryImpl;
import ru.otus.homework.repository.GenreRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookInsertUpdateServiceImpl.class, BookRepositoryImpl.class,
        AuthorRepositoryImpl.class, GenreRepositoryImpl.class})
class BookInsertUpdateServiceImplTest {
    @Autowired
    private BookInsertUpdateServiceImpl insertUpdateService;
    @Autowired
    private TestEntityManager em;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testSaveBookMethodWithParameters() {
        insertUpdateService.saveBook("Discipline and Punish", "Michel Foucault",
                "Philosophy");

        final Book actualBook = em.find(Book.class, 2L);
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("Discipline and Punish"))
                .matches(s -> s.getAuthor().getName().equals("Michel Foucault"))
                .matches(s -> s.getAuthor().getId()==2)
                .matches(s -> s.getGenre().getName().equals("Philosophy"))
                .matches(s -> s.getGenre().getId()==2);
    }

    @Test
    void testSaveBookMethodWithOldAuthorAndGenre(){
        insertUpdateService.saveBook("A Portrait of the Artist as a Young Man",
                "James Joyce", "Modernist novel");

        final Book actualBook = em.find(Book.class, 2L);
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("A Portrait of the Artist as a Young Man"))
                .matches(s -> s.getAuthor().getName().equals("James Joyce"))
                .matches(s -> s.getAuthor().getId()==1)
                .matches(s -> s.getGenre().getName().equals("Modernist novel"))
                .matches(s -> s.getGenre().getId()==1);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testUpdateBookMethodWithParameters() {
        insertUpdateService.updateBook(1L, "Discipline and Punish", "Michel Foucault",
                "Philosophy");

        final Book actualBook = em.find(Book.class, 1L);
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("Discipline and Punish"))
                .matches(s -> s.getAuthor().getName().equals("Michel Foucault"))
                .matches(s -> s.getAuthor().getId()==2)
                .matches(s -> s.getGenre().getName().equals("Philosophy"))
                .matches(s -> s.getGenre().getId()==2);
    }

    @Test
    void testUpdateBookMethodWithOldAuthorAndGenre(){
        insertUpdateService.updateBook(1L, "A Portrait of the Artist as a Young Man",
                "James Joyce", "Modernist novel");

        final Book actualBook = em.find(Book.class, 1L);
        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""))
                .matches(s -> s.getTitle().equals("A Portrait of the Artist as a Young Man"))
                .matches(s -> s.getAuthor().getName().equals("James Joyce"))
                .matches(s -> s.getAuthor().getId()==1)
                .matches(s -> s.getGenre().getName().equals("Modernist novel"))
                .matches(s -> s.getGenre().getId()==1);
    }
}