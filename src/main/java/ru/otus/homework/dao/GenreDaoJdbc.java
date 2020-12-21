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
    public void insert(Genre genre) {
        namedParameterJdbcOperations.update("insert into genre (title) values (:title)",
                Map.of("title", genre.getTitle()));
    }

    @Override
    public Genre getGenreById(long id) {
        return namedParameterJdbcOperations.queryForObject(
                "select * from genre where id = :id", Map.of("id", id), new GenreMapper()
        );
    }

    @Override
    public Genre getGenreByTitle(String title) {
        return namedParameterJdbcOperations.queryForObject(
                "select * from genre where title = :title", Map.of("title", title), new GenreMapper()
        );
    }

    @Override
    public List<Genre> getAll(){
        return namedParameterJdbcOperations.query("select * from genre", new GenreMapper());
    }

    @Override
    public void update(Genre genre) {
        namedParameterJdbcOperations.update("update genre set title = :title where id = :id",
                Map.of("title", genre.getTitle(), "id", genre.getId()));
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update(
                "delete from genre where id = :id", Map.of("id", id)
        );
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            return new Genre(id, title);
        }
    }
}
