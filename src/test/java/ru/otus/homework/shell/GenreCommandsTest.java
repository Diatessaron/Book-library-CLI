package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.GenreRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class GenreCommandsTest {
    @MockBean
    private GenreRepositoryImpl genreRepository;

    @Autowired
    private Shell shell;
    private final Genre novel = new Genre(1, "Modernist novel");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertMethodByTimesOfDaoInvocation() {
        shell.evaluate(() -> "gInsert 2 Philosophy");

        verify(genreRepository, times(1)).save(new Genre(2, "Philosophy"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessage(){
        final String expected = "You successfully saved a philosophy to repository";
        final String actual = shell.evaluate(() -> "gInsert 2 philosophy").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetGenreByIdByMessageComparison() {
        when(genreRepository.getGenreById(1)).thenReturn(Optional.of(novel));
        final String expected = Optional.of(novel).toString();
        final String actual = shell.evaluate(() -> "genreById 1").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAuthorByNameByMessageComparison() {
        when(genreRepository.getGenreByName(novel.getName())).thenReturn(novel);
        final String expected = novel.toString();
        final String actual = shell.evaluate(() -> "genreByTitle Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(genreRepository.getAll()).thenReturn(List.of(novel));
        final String expected = List.of(novel).toString();
        final String actual = shell.evaluate(() -> "gGetAll").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethod() {
        final String expected = "Modernist novel was updated";
        final String actual = shell.evaluate(() -> "gUpdate 1 Modernist,novel").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(genreRepository.getGenreById(1)).thenReturn(Optional.of(novel));

        final String expected = "Modernist novel was deleted";
        final String actual = shell.evaluate(() -> "gDelete 1").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void bookShouldBeDeletedBeforeGenreDeletion(){
        final String genreName = novel.getName();
        when(genreRepository.getGenreById(1)).thenReturn(Optional.of(novel));
        shell.evaluate(() -> "gDelete 1");

        final InOrder inOrder = inOrder(genreRepository);
        inOrder.verify(genreRepository).getGenreById(1);
        inOrder.verify(genreRepository).deleteById(1);
    }
}
