package ru.otus.homework.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository{
    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return (long) em.createQuery("select count(c) from Comment c").getSingleResult();
    }

    @Transactional
    @Override
    public Comment save(Comment comment) {
        if(comment.getId()==0){
            em.persist(comment);
            return comment;
        }
        else{
            return em.merge(comment);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> getCommentById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getCommentByContent(String content) {
        final TypedQuery<Comment> query = em.createQuery
                ("select c from Comment c where c.content = :content", Comment.class);
        query.setParameter("content", content);
        return query.getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    @Transactional
    @Override
    public void update(Comment comment) {
        final Query query = em.createQuery("update Comment c set c.content = :content where c.id = :id");
        query.setParameter("id", comment.getId());
        query.setParameter("content", comment.getContent());
        query.executeUpdate();
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        final Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
