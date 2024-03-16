package ru.yandex.practicum.filmorate.storage.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;


@Component
public class UtilsDbStorage {
    public static final String SELECT_GENRE_BY_ID = "SELECT * FROM GENRE_DICTIONARY where id = ?";
    public static final String SELECT_MPA_BY_ID = "select * from MPA_DICTIONARY where id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UtilsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Mpa> getAllMpa() {
        String sql = "select * from MPA_DICTIONARY order by ID";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> Mpa.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("TITLE"))
                        .description(rs.getString("description"))
                        .build());
    }

    public Collection<Genre> getAllGenres() {
        String sql = "select * from GENRE_DICTIONARY order by ID";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> Genre.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("NAME"))
                        .build());
    }

    public Optional<Mpa> getMpaById(Integer mpaId) {
        try {
            Mpa mpa = jdbcTemplate.queryForObject(
                    SELECT_MPA_BY_ID,
                    (rs, rowNum) ->
                            Mpa.builder()
                                    .id(rs.getInt("id"))
                                    .name(rs.getString("title"))
                                    .description(rs.getString("description"))
                                    .build(),
                    mpaId
            );
            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        try {
            Genre genre = jdbcTemplate.queryForObject(
                    SELECT_GENRE_BY_ID,
                    (rs, rowNum) -> Genre.builder()
                            .id(rs.getInt("ID"))
                            .name(rs.getString("NAME"))
                            .build(),
                    genreId);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
