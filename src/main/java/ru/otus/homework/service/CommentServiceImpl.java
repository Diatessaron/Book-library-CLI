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
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final MongoTemplate mongoTemplate;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository,
                              MongoTemplate mongoTemplate) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    @Override
    public String saveComment(String bookTitle, String commentContent) {
        final List<Book> bookList = bookRepository.findByTitle(bookTitle);

        if(bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if(bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect book title");

        final String book = bookList.get(0).getTitle();
        final Comment comment = new Comment(commentContent, book);

        commentRepository.save(comment);

        return "You successfully added a comment to " + book;
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getCommentByContent(String content) {
        return commentRepository.findByContent(content).orElseThrow
                (() -> new IllegalArgumentException("Incorrect comment content"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByBook(String bookTitle) {
        final List<Book> bookList = bookRepository.findByTitle(bookTitle);

        if(bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if(bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect book title");

        final Book book = bookList.get(0);

        return commentRepository.findByBookTitle(book.getTitle());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Transactional
    @Override
    public String updateComment(String oldCommentContent, String commentContent) {
        final List<Book> bookList = bookRepository.findByTitle(commentRepository.findByContent
                (oldCommentContent).orElseThrow
                (() -> new IllegalArgumentException("Incorrect old comment content")).getBookTitle());

        if (bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if (bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect book title");

        final Book book = bookList.get(0);

        Query query = new Query();
        query.addCriteria(Criteria.where("content").is(oldCommentContent));
        Update update = new Update();
        update.set("content", commentContent);
        mongoTemplate.updateFirst(query, update, Comment.class);

        return book.getTitle() + " comment was updated";
    }

    @Transactional
    @Override
    public String deleteByContent(String content) {
        final List<Book> bookList = bookRepository.findByTitle(commentRepository.findByContent(content)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect comment content")).getBookTitle());

        if (bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if (bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect book title");

        final Book book = bookList.get(0);

        commentRepository.deleteByContent(content);

        return book.getTitle() + " comment was deleted";
    }
}
