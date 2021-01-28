package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository,
                           GenreRepository genreRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public void saveBook(String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(title, author, genre);

        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByTitle(String title) {
        return getBook(bookRepository.findByTitle(title));
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByAuthor(String author) {
        return getBook(bookRepository.findByAuthor_Name(author));
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByGenre(String genre) {
        return getBook(bookRepository.findByGenre_Name(genre));
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByComment(String comment) {
        final List<Book> bookList = bookRepository.findByTitle(commentRepository.findByContent(comment)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect book comment")).getBook().getTitle());

        return getBook(bookList);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public void updateBook(String oldBookTitle, String title, String authorNameParameter,
                           String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);

        final Book book = getBook(bookRepository.findByTitle(oldBookTitle));
        book.setAuthor(author);
        book.setGenre(genre);
        book.setTitle(title);

        bookRepository.save(book);

        final List<Comment> commentList = commentRepository.findByBook_Title(oldBookTitle);

        commentList.forEach(c -> c.setBook(title));
        commentRepository.saveAll(commentList);
    }

    @Transactional
    @Override
    public void deleteBookByTitle(String title) {
        bookRepository.deleteByTitle(title);
        commentRepository.deleteByBook_Title(title);
    }

    private Book getBook(List<Book> bookList){
        if (bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if (bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect book title");

        return bookList.get(0);
    }

    private Author getAuthor(String authorName) {
        final List<Author> authors = authorRepository.findByName(authorName);

        if (authors.isEmpty()) {
            final Author author = new Author(authorName);
            authorRepository.save(author);
            return author;
        } else
            return authors.get(0);
    }

    private Genre getGenre(String genreName) {
        final Optional<Genre> optionalGenre = genreRepository.findByName(genreName);

        if (optionalGenre.isEmpty()) {
            final Genre genre = new Genre(genreName);
            genreRepository.save(genre);
            return genre;
        } else
            return optionalGenre.get();
    }
}
