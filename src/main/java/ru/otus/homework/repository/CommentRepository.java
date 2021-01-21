package ru.otus.homework.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.homework.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Optional<Comment> findByContent(String content);

    List<Comment> findByBook_title(String bookTitle);

    @Query(value = "{'content': ?0}", fields = "{'book': 1, '_id': 0}")
    Optional<Comment> findBookByComment(String comment);

    List<Comment> findAll();

    void deleteByContent(String content);
}
