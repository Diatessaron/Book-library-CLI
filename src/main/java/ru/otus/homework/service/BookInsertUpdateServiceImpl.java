package ru.otus.homework.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;

import javax.persistence.NoResultException;

@Service
public class BookInsertUpdateServiceImpl implements BookInsertUpdateService{
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookInsertUpdateServiceImpl(BookRepository bookRepository,
                                       AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public void saveBook(String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(0L, title, author, genre);

        bookRepository.save(book);
    }

    @Override
    public void updateBook(long id, String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(id, title, author, genre);

        bookRepository.update(book);
    }

    private Author getAuthor(String authorName){
        try{
            return authorRepository.getAuthorByName(authorName);
        } catch (EmptyResultDataAccessException | NoResultException e){
            Author author = new Author(0L, authorName);
            authorRepository.save(author);
            return author;
        }
    }

    private Genre getGenre(String genreName){
        try{
            return genreRepository.getGenreByName(genreName);
        } catch (EmptyResultDataAccessException | NoResultException e){
            Genre genre = new Genre(0L, genreName);
            genreRepository.save(genre);
            return genre;
        }
    }
}
