package ru.otus.homework.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorDaoJdbc implements AuthorDao{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count(){
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject(
                "select count(*) from authors", Integer.class
        );
    }

    @Override
    public void insert(Author author) {
        namedParameterJdbcOperations.update("insert into authors (name) values (:name)",
                Map.of("name", author.getName()));
    }

    @Override
    public Author getAuthorById(long id) {
        return namedParameterJdbcOperations.queryForObject(
                "select id, name from authors where id = :id", Map.of("id", id), new AuthorMapper()
        );
    }

    @Override
    public Author getAuthorByName(String name) {
        return namedParameterJdbcOperations.queryForObject(
                "select id, name from authors where name = :name", Map.of("name", name), new AuthorMapper()
        );
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query("select id, name from authors", new AuthorMapper());
    }

    @Override
    public void update(Author author) {
        namedParameterJdbcOperations.update("update authors set name = :name where id = :id",
                Map.of("name", author.getName(), "id", author.getId()));
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update(
                "delete from authors where id = :id", Map.of("id", id)
        );
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Author(id, name);
        }
    }
}
