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
        final Book book = bookRepository.findByTitle(bookTitle).orElseThrow(
                () -> new IllegalArgumentException("Incorrect book title"));
        final Comment comment = new Comment(commentContent, book);

        commentRepository.save(comment);

        return "You successfully added a comment to " + book.getTitle();
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
        return commentRepository.findByBook_title(bookRepository.findByTitle(bookTitle)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect book name")).getTitle());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Transactional
    @Override
    public String updateComment(String oldCommentContent, String commentContent) {
        final Book book = commentRepository.findBookByComment(oldCommentContent).orElseThrow(
                () -> new IllegalArgumentException("Incorrect comment content")).getBook();

        final Comment comment = commentRepository.findByContent(oldCommentContent).orElseThrow
                (() -> new IllegalArgumentException("Incorrect comment content"));
        comment.setContent(commentContent);

        commentRepository.deleteByContent(oldCommentContent);
        commentRepository.save(comment);

        return book.getTitle() + " comment was updated";
    }

    @Transactional
    @Override
    public String deleteByContent(String content) {
        final Book book = commentRepository.findBookByComment(content)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect comment content")).getBook();
        commentRepository.deleteByContent(content);

        return book.getTitle() + " comment was deleted";
    }
}
