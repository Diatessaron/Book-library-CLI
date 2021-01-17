package ru.otus.homework.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;

import javax.persistence.NoResultException;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional
    @Override
    public void saveBook(String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(0L, title, author, genre);

        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookById(long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Incorrect book id"));
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByTitle(String title) {
        return bookRepository.findByTitle(title).orElseThrow
                (() -> new IllegalArgumentException("Incorrect book title"));
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByAuthor(String author) {
        return bookRepository.findByAuthor_Name(author).orElseThrow
                (() -> new IllegalArgumentException("Incorrect author name"));
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByGenre(String genre) {
        return bookRepository.findByGenre_Name(genre).orElseThrow
                (() -> new IllegalArgumentException("Incorrect genre name"));
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByComment(String comment) {
        return bookRepository.findByComment_Content(comment).orElseThrow
                (() -> new IllegalArgumentException("Incorrect comment content"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public void updateBook(long id, String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(id, title, author, genre);

        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBookById(long id) {
        bookRepository.deleteById(id);
    }

    private Author getAuthor(String authorName) {
        final Author author = authorRepository.findByName(authorName)
                .orElse(new Author(0L, authorName));

        if (author.getId() == 0L)
            authorRepository.save(author);

        return author;
    }

    private Genre getGenre(String genreName) {
        final Genre genre = genreRepository.findByName(genreName)
                .orElse(new Genre(0L, genreName));

        if (genre.getId() == 0L)
            genreRepository.save(genre);

        return genre;
    }
}
