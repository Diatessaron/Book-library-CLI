package ru.otus.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthorServiceImplTest {
    @Autowired
    private AuthorServiceImpl service;

    private final Author jamesJoyce = new Author("James Joyce");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testSaveByComparing() {
        final Author foucault = new Author("Michel Foucault");
        service.saveAuthor(foucault.getName());

        final Author actual = service.getAuthorByName("Michel Foucault");

        assertEquals(foucault.getName(), actual.getName());
    }

    @Test
    void shouldReturnCorrectAuthorByName() {
        final Author actual = service.getAuthorByName(jamesJoyce.getName());

        assertEquals(jamesJoyce, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateAuthorMethodByComparing() {
        service.updateAuthor("James Joyce", "Author");

        final Author actualAuthor = service.getAuthorByName("Author");
        assertThat(actualAuthor).isNotNull().matches(s -> !s.getName().isBlank())
                .matches(s -> s.getName().equals("Author"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void authorShouldBeDeletedCorrectly() {
        final String expected = "James Joyce was deleted";
        final String actual = service.deleteAuthorByName("James Joyce");

        assertEquals(expected, actual);
    }
}
