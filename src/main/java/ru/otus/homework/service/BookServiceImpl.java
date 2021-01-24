package ru.otus.homework.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private final MongoTemplate mongoTemplate;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository,
                           GenreRepository genreRepository, CommentRepository commentRepository,
                           MongoTemplate mongoTemplate) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
        this.mongoTemplate = mongoTemplate;
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
        final List<Book> bookList = bookRepository.findByTitle(title);

        if (bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if (bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect book title");

        return bookList.get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByAuthor(String author) {
        final List<Book> bookList = bookRepository.findByAuthor_Name(author);

        if (bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if(bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect author name");

        return bookList.get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByGenre(String genre) {
        final List<Book> bookList = bookRepository.findByGenre_Name(genre);

        if (bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if(bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect genre name");

        return bookList.get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByComment(String comment) {
        final List<Book> bookList = bookRepository.findByTitle(commentRepository.findByContent(comment)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect book comment")).getBookTitle());

        if (bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if (bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect book title");

        return bookList.get(0);
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

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(oldBookTitle));
        Update update = new Update();
        update.set("title", title);
        update.set("author", author);
        update.set("genre", genre);
        mongoTemplate.updateFirst(query, update, Book.class);

        query = new Query();
        query.addCriteria(Criteria.where("bookTitle").is(oldBookTitle));
        update = new Update();
        update.set("bookTitle", title);
        mongoTemplate.updateFirst(query, update, Comment.class, "comments");
    }

    @Transactional
    @Override
    public void deleteBookByTitle(String title) {
        bookRepository.deleteByTitle(title);

        Query query = new Query();
        query.addCriteria(Criteria.where("bookTitle").is(title));
        mongoTemplate.remove(query, Comment.class, "comments");
    }

    private Author getAuthor(String authorName) {
        final Optional<Author> optionalAuthor = authorRepository.findByName(authorName);

        if (optionalAuthor.isEmpty()) {
            final Author author = new Author(authorName);
            authorRepository.save(author);
            return author;
        } else
            return optionalAuthor.get();
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
