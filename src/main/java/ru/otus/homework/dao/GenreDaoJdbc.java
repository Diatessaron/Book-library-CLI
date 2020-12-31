package ru.otus.homework.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class GenreDaoJdbc implements GenreDao{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count(){
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject(
                "select count(*) from genres", Integer.class
        );
    }

    @Override
    public void insert(Genre genre) {
        namedParameterJdbcOperations.update("insert into genres (name) values (:name)",
                Map.of("name", genre.getName()));
    }

    @Override
    public Genre getGenreById(long id) {
        return namedParameterJdbcOperations.queryForObject(
                "select id, name from genres where id = :id", Map.of("id", id), new GenreMapper()
        );
    }

    @Override
    public Genre getGenreByName(String name) {
        return namedParameterJdbcOperations.queryForObject(
                "select id, name from genres where name = :name", Map.of("name", name), new GenreMapper()
        );
    }

    @Override
    public List<Genre> getAll(){
        return namedParameterJdbcOperations.query("select id, name from genres", new GenreMapper());
    }

    @Override
    public void update(Genre genre) {
        namedParameterJdbcOperations.update("update genres set name = :name where id = :id",
                Map.of("name", genre.getName(), "id", genre.getId()));
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update(
                "delete from genres where id = :id", Map.of("id", id)
        );
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Genre(id, name);
        }
    }
}
