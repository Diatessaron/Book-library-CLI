package ru.otus.homework.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

@Service
public class BookInsertUpdateServiceImpl implements BookInsertUpdateService{
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public BookInsertUpdateServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public void insertBook(long id, String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(id, title, author, genre);

        bookDao.insert(book);
    }

    @Override
    public void updateBook(long id, String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(id, title, author, genre);

        bookDao.update(book);
    }

    private Author getAuthor(String authorName){
        try{
            return authorDao.getAuthorByName(authorName);
        } catch (EmptyResultDataAccessException e){
            Author author = new Author(authorDao.count()+1L, authorName);
            authorDao.insert(author);
            return author;
        }
    }

    private Genre getGenre(String genreName){
        try{
            return genreDao.getGenreByName(genreName);
        } catch (EmptyResultDataAccessException e){
            Genre genre = new Genre(genreDao.count()+1L, genreName);
            genreDao.insert(genre);
            return genre;
        }
    }
}
