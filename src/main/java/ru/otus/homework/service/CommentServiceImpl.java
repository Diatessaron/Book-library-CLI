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
    public String saveComment(long bookId, String commentContent) {
        final Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id"));
        final Comment comment = new Comment(0L, commentContent, book);

        commentRepository.save(comment);
        bookRepository.save(book);

        return "You successfully added a comment to " + book.getTitle();
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getCommentById(long id) {
        return commentRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect id"));
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
        return commentRepository.findByBook_Id(bookRepository.findByTitle(bookTitle)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect book name")).getId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Transactional
    @Override
    public String updateComment(long bookId, long commentId, String commentContent) {
        final Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id"));

        commentRepository.update(commentContent, book, commentId);

        return book.getTitle() + " comment was updated";
    }

    @Transactional
    @Override
    public String deleteById(long id) {
        final Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Incorrect comment id"));

        final Book book = bookRepository.findByComment_Content(comment.getContent())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect comment content"));
        commentRepository.deleteById(id);

        return book.getTitle() + " comment was deleted";
    }
}
