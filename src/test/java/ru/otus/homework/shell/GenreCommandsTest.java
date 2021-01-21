package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class GenreCommandsTest {
    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private Shell shell;
    private final Genre novel = new Genre("Modernist novel");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertMethodByTimesOfRepositoryInvocation() {
        shell.evaluate(() -> "gInsert Philosophy");

        verify(genreRepository, times(1)).save(new Genre("Philosophy"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessage() {
        final String expected = "You successfully saved a philosophy to repository";
        final String actual = shell.evaluate(() -> "gInsert philosophy").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetGenreByNameByMessageComparison() {
        when(genreRepository.findByName(novel.getName())).thenReturn(Optional.of(novel));
        final String expected = novel.toString();
        final String actual = shell.evaluate(() -> "genreByName Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(genreRepository.findAll()).thenReturn(List.of(novel));
        final String expected = List.of(novel).toString();
        final String actual = shell.evaluate(() -> "gGetAll").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterUpdateMethod() {
        when(genreRepository.findByName("Modernist novel")).thenReturn(Optional.of(novel));
        final String expected = "Philosophy was updated";
        final String actual = shell.evaluate(() -> "gUpdate Modernist,novel Philosophy").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(genreRepository.findByName("Modernist novel")).thenReturn(Optional.of(novel));

        final String expected = "Modernist novel was deleted";
        final String actual = shell.evaluate(() -> "gDelete Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void bookShouldBeDeletedBeforeGenreDeletion() {
        when(genreRepository.findByName("Modernist novel")).thenReturn(Optional.of(novel));
        shell.evaluate(() -> "gDelete Modernist,novel");

        final InOrder inOrder = inOrder(genreRepository);
        inOrder.verify(genreRepository).findByName("Modernist novel");
        inOrder.verify(genreRepository).deleteByName("Modernist novel");
    }
}
