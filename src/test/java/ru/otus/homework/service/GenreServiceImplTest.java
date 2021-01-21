package ru.otus.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenreServiceImplTest {
    @Autowired
    private GenreServiceImpl service;

    private final Genre expectedNovel = new Genre("Modernist novel");

    @BeforeEach
    void setUp(){
        service.saveGenre(expectedNovel.getName());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSaveByComparing() {
        final Genre philosophy = new Genre("Philosophy");
        service.saveGenre(philosophy.getName());

        final Genre actual = service.getGenreByName("Philosophy");

        assertEquals(philosophy.getName(), actual.getName());
    }

    @Test
    void shouldReturnCorrectGenreByName() {
        final Genre actual = service.getGenreByName(expectedNovel.getName());

        assertEquals(expectedNovel, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetGenreByNameMethodInvocation(){
        assertThrows(IllegalArgumentException.class, () -> service.getGenreByName("genre"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectListOfGenre(){
        final Genre philosophy = new Genre("Philosophy");
        final List<Genre> expected = List.of(expectedNovel, philosophy);

        service.saveGenre(philosophy.getName());

        final List<Genre> actual = service.getAll();

        assertThat(actual).isNotNull().matches(a -> a.size() == expected.size())
                .matches(a -> a.get(0).getName().equals(expected.get(0).getName()))
                .matches(a -> a.get(1).getName().equals(expected.get(1).getName()));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateAuthorMethodByComparing() {
        service.updateGenre("Modernist novel", "Genre");

        final Genre actualGenre = service.getGenreByName("Genre");
        assertThat(actualGenre).isNotNull().matches(s -> !s.getName().isBlank())
                .matches(s -> s.getName().equals("Genre"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void authorShouldBeDeletedCorrectly() {
        service.saveGenre("Genre");

        final String expected = "Genre was deleted";
        final String actual = service.deleteGenreByName("Genre");

        assertEquals(expected, actual);
    }
}
