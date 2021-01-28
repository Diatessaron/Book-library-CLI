package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Override
    public String saveComment(String bookTitle, String commentContent) {
        final String book = getBook(bookRepository.findByTitle(bookTitle)).getTitle();
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
        final Book book = getBook(bookRepository.findByTitle(bookTitle));

        return commentRepository.findByBook_Title(book.getTitle());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Transactional
    @Override
    public String updateComment(String oldCommentContent, String commentContent) {
        final Comment comment = commentRepository.findByContent
                (oldCommentContent).orElseThrow
                (() -> new IllegalArgumentException("Incorrect old comment content"));

        final Book book = getBook(bookRepository.findByTitle(comment.getBook().getTitle()));
        comment.setContent(commentContent);
        comment.setBook(book.getTitle());

        commentRepository.save(comment);

        return book.getTitle() + " comment was updated";
    }

    @Transactional
    @Override
    public String deleteByContent(String content) {
        final List<Book> bookList = bookRepository.findByTitle(commentRepository.findByContent(content)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect comment content")).getBook().getTitle());

        final Book book = getBook(bookList);

        commentRepository.deleteByContent(content);

        return book.getTitle() + " comment was deleted";
    }

    private Book getBook(List<Book> bookList){
        if (bookList.size() > 1)
            throw new IllegalArgumentException("Not unique result. Please, specify correct argument.");
        else if (bookList.isEmpty())
            throw new IllegalArgumentException("Incorrect book title");

        return bookList.get(0);
    }
}
