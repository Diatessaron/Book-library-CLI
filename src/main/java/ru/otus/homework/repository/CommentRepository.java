package ru.otus.homework.repository;

import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    long count();

    Comment save(Comment comment);

    Optional<Comment> getCommentById(long id);

    Comment getCommentByContent(String content);

    List<Comment> getCommentsByBook(Book book);

    List<Comment> getAll();

    void update(Comment comment);

    void deleteById(long id);
}
