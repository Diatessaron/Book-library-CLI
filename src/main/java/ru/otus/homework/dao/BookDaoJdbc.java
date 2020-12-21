package ru.otus.homework.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class BookDaoJdbc implements BookDao{
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public void insert(Book book) {
        namedParameterJdbcOperations.update("insert into book (title, author, genre) values (:title, :author, :genre)",
                Map.of("title", book.getTitle(), "author", book.getAuthor(), "genre", book.getGenre()));
    }

    @Override
    public Book getBookById(long id) {
        return namedParameterJdbcOperations.queryForObject(
                "select * from book where id = :id", Map.of("id", id), new BookMapper()
        );
    }

    @Override
    public Book getBookByTitle(String title) {
        return namedParameterJdbcOperations.queryForObject(
                "select * from book where title = :title", Map.of("title", title), new BookMapper()
        );
    }

    @Override
    public Book getBookByAuthor(String author) {
        return namedParameterJdbcOperations.queryForObject(
                "select * from book where author = :author", Map.of("author", author), new BookMapper()
        );
    }

    @Override
    public Book getBookByGenre(String genre) {
        return namedParameterJdbcOperations.queryForObject(
          "select * from book where genre = :genre", Map.of("genre", genre), new BookMapper()
        );
    }

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query("select * from book", new BookMapper());
    }

    @Override
    public void update(Book book) {
        namedParameterJdbcOperations.update("update book set title = :title, author = :author, " +
                        "genre = :genre where id = :id", Map.of("title", book.getTitle(),
                "author", book.getAuthor(), "genre", book.getGenre(), "id", book.getId()));
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from book where id = :id", Map.of("id", id));
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            final String author = resultSet.getString("author");
            final String genre = resultSet.getString("genre");
            return new Book(id, title, author, genre);
        }
    }
}
