package ru.otus.homework.repository;

import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return (long) em.createQuery("select count(c) from Comment c").getSingleResult();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public Optional<Comment> getCommentById(long id) {
        final TypedQuery<Comment> query = em.createQuery("select c from Comment c join fetch " +
                "c.book b join fetch b.genre g join fetch b.author a where c.id = :id", Comment.class);
        query.setParameter("id", id);

        Comment result = null;

        try {
            result = query.getSingleResult();
        } catch (NoResultException ignored) {
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Comment getCommentByContent(String content) {
        final TypedQuery<Comment> query = em.createQuery
                ("select c from Comment c join fetch c.book b join fetch b.genre g join fetch " +
                        "b.author a where c.content = :content", Comment.class);
        query.setParameter("content", content);

        return query.getSingleResult();
    }

    @Override
    public List<Comment> getCommentsByBook(Book book) {
        final Query query = em.createQuery("select c from Comment c join fetch c.book b " +
                "join fetch b.genre g join fetch b.author a where c.book = :book");
        query.setParameter("book", book);

        return query.getResultList();
    }

    @Override
    public List<Comment> getAll() {
        return em.createQuery("select c from Comment c join fetch c.book b join fetch b.genre g " +
                "join fetch b.author a", Comment.class).getResultList();
    }

    @Override
    public void update(Comment comment) {
        final Query query = em.createQuery("update Comment c set c.content = :content, c.book = :book " +
                "where c.id = :id");
        query.setParameter("id", comment.getId());
        query.setParameter("content", comment.getContent());
        query.setParameter("book", comment.getBook());

        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        final Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}