package ru.otus.homework.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Author;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository repository;
    @Autowired
    private TestEntityManager em;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Author expected = new Author(1, "Michel Foucault");
        repository.update(expected.getName(), expected.getId());
        final Author actual = em.find(Author.class, 1L);

        assertEquals(expected, actual);
    }
}
