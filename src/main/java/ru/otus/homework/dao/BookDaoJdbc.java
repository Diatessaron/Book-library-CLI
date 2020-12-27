package ru.otus.homework.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class BookDaoJdbc implements BookDao{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public void insert(Book book) {
        namedParameterJdbcOperations.update("insert into book (title, author_id, genre_id) " +
                        "values (:title, :author_id, :genre_id)",
                Map.of("title", book.getTitle(), "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId()));
    }

    @Override
    public Book getBookById(long id) {
        return namedParameterJdbcOperations.queryForObject(
                "select b.id, b.title, a.id author_id, a.name author_name, g.id genre_id, g.name genre_name " +
                        "from book b left join author a on b.author_id = a.id left join genre g on b.genre_id = g.id " +
                        "where b.id = :id",
                Map.of("id", id), new BookMapper()
        );
    }

    @Override
    public Book getBookByTitle(String title) {
        return namedParameterJdbcOperations.queryForObject(
                "select b.id, b.title, a.id author_id, a.name author_name, g.id genre_id, g.name genre_name " +
                        "from book b left join author a on b.author_id = a.id left join genre g on b.genre_id = g.id " +
                        "where title = :title",
                Map.of("title", title),
                new BookMapper()
        );
    }

    @Override
    public Book getBookByAuthor(String authorName) {
        return namedParameterJdbcOperations.queryForObject(
                "select b.id, b.title, a.id author_id, a.name author_name, g.id genre_id, g.name genre_name " +
                        "from book b left join author a on b.author_id = a.id left join genre g on b.genre_id = g.id " +
                        "where a.name = :author_name", Map.of("author_name", authorName), new BookMapper()
        );
    }

    @Override
    public Book getBookByGenre(String genreName) {
        return namedParameterJdbcOperations.queryForObject(
          "select b.id, b.title, a.id author_id, a.name author_name, g.id genre_id, g.name genre_name " +
                  "from book b left join author a on b.author_id = a.id left join genre g on b.genre_id = g.id " +
                  "where g.name = :genre_name", Map.of("genre_name", genreName), new BookMapper()
        );
    }

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query("select b.id, b.title, a.id author_id, a.name author_name, " +
                "g.id genre_id, g.name genre_name from book b left join author a on b.author_id = a.id " +
                "left join genre g on b.genre_id = g.id", new BookMapper());
    }

    @Override
    public void update(Book book) {
        namedParameterJdbcOperations.update("update book set title = :title, author_id = :author_id, " +
                "genre_id = :genre_id where id = :id", Map.of("title", book.getTitle(),
            "author_id", book.getAuthor().getId(), "genre_id", book.getGenre().getId(), "id", book.getId()));
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from book where id = :id", Map.of("id", id));
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final String title = resultSet.getString("title");
            final Author author = new Author(resultSet.getLong("author_id"),
                    resultSet.getString("author_name"));
            final Genre genre = new Genre(resultSet.getLong("genre_id"),
                    resultSet.getString("genre_name"));
            return new Book(id, title, author, genre);
        }
    }
}
