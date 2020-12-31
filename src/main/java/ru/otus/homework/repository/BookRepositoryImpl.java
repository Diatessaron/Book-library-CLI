package ru.otus.homework.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository{
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Book save(Book book) {
        if(book.getId()==0 && book.getAuthor().getId()==0 && book.getGenre().getId()==0){
            em.persist(book);
            return book;
        }
        else{
            return em.merge(book);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> getBookById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByTitle(String title) {
        final TypedQuery<Book> query = em.createQuery
                ("select b from Book b where b.title = :title", Book.class);
        query.setParameter("title", title);
        return query.getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByAuthor(String author) {
        final TypedQuery<Book> query = em.createQuery
                ("select b from Book b where b.author.name = :author", Book.class);
        query.setParameter("author", author);
        return query.getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByGenre(String genre) {
        final TypedQuery<Book> query = em.createQuery
                ("select b from Book b where b.genre.name = :genre", Book.class);
        query.setParameter("genre", genre);
        return query.getSingleResult();
    }

    @Transactional(readOnly = true)
    //TODO: factor to solve N+1 and check other
    @Override
    public List<Book> getAll() {
        return em.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Transactional
    @Override
    public void update(Book book) {
        final Query query = em.createQuery(
                "update Book b set b.title = :title, b.author = :author, b.genre = :genre " +
                        "where b.id = :id"
        );
        query.setParameter("id", book.getId());
        query.setParameter("title", book.getTitle());
        query.setParameter("author", book.getAuthor());
        query.setParameter("genre", book.getGenre());

        final Author author = book.getAuthor();
        final Genre genre = book.getGenre();

        if(author.getId()==0)
            em.persist(author);

        if(genre.getId()==0)
            em.persist(genre);

        query.executeUpdate();
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        final Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
