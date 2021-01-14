package ru.otus.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByContent(String content);

    List<Comment> findByBook_Id(long bookId);

    @Modifying
    @Query("update Comment c set c.content = :content, c.book = :book where c.id = :id")
    void update(String content, Book book, long id);
}
