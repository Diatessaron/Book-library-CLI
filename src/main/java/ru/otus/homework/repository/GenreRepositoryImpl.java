package ru.otus.homework.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryImpl implements GenreRepository{
    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return (long) em.createQuery("select count(g) from Genre g").getSingleResult();
    }

    @Transactional
    @Override
    public Genre save(Genre genre) {
        if(genre.getId()==0){
            em.persist(genre);
            return genre;
        }
        else{
            return em.merge(genre);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> getGenreById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenreByName(String name) {
        final TypedQuery<Genre> query = em.createQuery
                ("select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    //TODO: factor to solve N+1 and check other
    @Transactional(readOnly = true)
    @Override
    public List<Genre> getAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Transactional
    @Override
    public void update(Genre genre) {
        final Query query = em.createQuery("update Genre g set g.name = :name where g.id = :id");
        query.setParameter("id", genre.getId());
        query.setParameter("name", genre.getName());
        query.executeUpdate();
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        final Query query = em.createQuery("delete from Genre g where g.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
