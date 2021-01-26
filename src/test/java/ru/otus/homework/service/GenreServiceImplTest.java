package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GenreServiceImplTest {
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private GenreServiceImpl service;

    private final Genre expectedNovel = new Genre("Modernist novel");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSaveByComparing() {
        final Genre philosophy = new Genre("Philosophy");

        when(genreRepository.save(philosophy)).thenReturn(philosophy);
        when(genreRepository.findByName(philosophy.getName())).thenReturn(java.util.Optional.of(philosophy));

        service.saveGenre(philosophy.getName());

        final Genre actual = service.getGenreByName("Philosophy");

        assertEquals(philosophy.getName(), actual.getName());

        verify(genreRepository, times(1)).save(philosophy);
    }

    @Test
    void shouldReturnCorrectGenreByName() {
        when(genreRepository.findByName(expectedNovel.getName())).thenReturn(java.util.Optional.of(expectedNovel));

        final Genre actual = service.getGenreByName(expectedNovel.getName());

        assertEquals(expectedNovel, actual);

        verify(genreRepository, times(1)).findByName(expectedNovel.getName());
    }

    @Test
    void shouldThrowExceptionAfterGetGenreByNameMethodInvocation() {
        assertThrows(IllegalArgumentException.class, () -> service.getGenreByName("genre"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectListOfGenre() {
        final Genre philosophy = new Genre("Philosophy");
        final List<Genre> expected = List.of(expectedNovel, philosophy);

        when(genreRepository.save(philosophy)).thenReturn(philosophy);
        when(genreRepository.findAll()).thenReturn(expected);

        service.saveGenre(philosophy.getName());

        final List<Genre> actual = service.getAll();

        assertThat(actual).isNotNull().matches(a -> a.size() == expected.size())
                .matches(a -> a.get(0).getName().equals(expected.get(0).getName()))
                .matches(a -> a.get(1).getName().equals(expected.get(1).getName()));

        verify(genreRepository, times(1)).findAll();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateGenreMethodByComparing() {
        final Genre genre = new Genre("Genre");

        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        when(genreRepository.findByName(expectedNovel.getName())).thenReturn(Optional.of(expectedNovel));
        when(genreRepository.save(genre)).thenReturn(genre);
        when(bookRepository.findByGenre_Name(expectedNovel.getName())).thenReturn(List.of());

        service.updateGenre("Modernist novel", "Genre");

        final Genre actualGenre = service.getGenreByName("Genre");
        assertThat(actualGenre).isNotNull().matches(s -> !s.getName().isBlank())
                .matches(s -> s.getName().equals("Genre"));

        final InOrder inOrder = inOrder(genreRepository, bookRepository);
        inOrder.verify(genreRepository).findByName("Modernist novel");
        inOrder.verify(genreRepository).save(genre);
        inOrder.verify(bookRepository).findByGenre_Name("Modernist novel");
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void genreShouldBeDeletedCorrectly() {
        when(genreRepository.findByName(expectedNovel.getName())).thenReturn(Optional.of(expectedNovel));
        doNothing().when(genreRepository).deleteByName(expectedNovel.getName());
        doNothing().when(bookRepository).deleteByGenre_Name(expectedNovel.getName());

        final String expected = "Modernist novel was deleted";
        final String actual = service.deleteGenreByName("Modernist novel");

        assertEquals(expected, actual);

        final InOrder inOrder = inOrder(genreRepository, bookRepository);
        inOrder.verify(genreRepository).findByName(expectedNovel.getName());
        inOrder.verify(genreRepository).deleteByName(expectedNovel.getName());
        inOrder.verify(bookRepository).deleteByGenre_Name(expectedNovel.getName());
    }
}
