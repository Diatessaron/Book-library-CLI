package ru.otus.homework.shell;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;
import ru.otus.homework.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorCommandsTest {
    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private Shell shell;
    public final Author jamesJoyce = new Author("James Joyce");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessage() {
        final String expected = "You successfully saved a Michel Foucault to repository";
        final String actual = shell.evaluate(() -> "aInsert Michel,Foucault").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAuthorByNameByMessageComparison() {
        when(authorRepository.findByName(jamesJoyce.getName())).thenReturn(Optional.of(jamesJoyce));
        final String expected = jamesJoyce.toString();
        final String actual = shell.evaluate(() -> "authorByName James,Joyce").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(authorRepository.findAll()).thenReturn(List.of(jamesJoyce));
        final String expected = List.of(jamesJoyce).toString();
        final String actual = shell.evaluate(() -> "aGetAll").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethod() {
        when(authorRepository.findByName("James Joyce")).thenReturn(Optional.of(jamesJoyce));

        final String expected = "Michel Foucault was updated";
        final String actual = shell.evaluate(() -> "aUpdate James,Joyce Michel,Foucault").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(authorRepository.findByName("James Joyce")).thenReturn(Optional.of(jamesJoyce));

        final String expected = "James Joyce was deleted";
        final String actual = shell.evaluate(() -> "aDelete James,Joyce").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void bookShouldBeDeletedBeforeAuthorDeletion() {
        when(authorRepository.findByName("James Joyce")).thenReturn(Optional.of(jamesJoyce));
        shell.evaluate(() -> "aDelete James,Joyce");

        final InOrder inOrder = inOrder(authorRepository);
        inOrder.verify(authorRepository).findByName("James Joyce");
        inOrder.verify(authorRepository).deleteByName("James Joyce");
    }
}
