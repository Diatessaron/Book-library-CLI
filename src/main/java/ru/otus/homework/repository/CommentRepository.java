package ru.otus.homework.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Optional<Comment> findByContent(String content);

    List<Comment> findByBook_Title(String bookTitle);

    void deleteByContent(String content);

    void deleteByBook_Title(String bookTitle);
}
