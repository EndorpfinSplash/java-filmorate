package ru.yandex.practicum.filmorate.storage.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.util.*;


@Component
public class UtilsDbStorage {
    public static final String SELECT_GENRE_BY_ID = "select * from GENRE_DICTIONARY where id = ?";
    public static final String SELECT_TITLE_BY_ID = "select * from MPA_DICTIONARY where id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UtilsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Mpa> getAllMpa() {
        String sql = "select * from GENRE_DICTIONARY";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> Mpa.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .build());
    }

    public Collection<Genre> getAllGenres() {
        String sql = "select * from GENRE_DICTIONARY";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> Genre.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build());
    }

    public Optional<Mpa> getMpaById(Integer mpaId) {
        Mpa mpa = jdbcTemplate.queryForObject(
                SELECT_TITLE_BY_ID,
                new Integer[]{mpaId},
                (rs, rowNum) ->
                        Mpa.builder()
                                .id(rs.getInt("id"))
                                .name(rs.getString("title"))
                                .description(rs.getString("description"))
                                .build()
        );
        return Optional.ofNullable(mpa);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        Genre genre = jdbcTemplate.queryForObject(
                SELECT_GENRE_BY_ID,
                (rs, rowNum) -> Genre.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("title"))
                        .build(),
                genreId);
        return Optional.ofNullable(genre);
    }

    public Set<Integer> getUserFriendsId(Integer userId) {
        String sql =
                "select APPROVER from FRIENDSHIP where INITIATOR = ? and APPROVE_DATE is not null " +
                        "union " +
                        "select INITIATOR from FRIENDSHIP where APPROVER = ? and APPROVE_DATE is null ";
        List<Integer> friendsIds = jdbcTemplate.query(sql, new Integer[]{userId, userId}, ResultSet::getInt);
        return new HashSet<>(friendsIds);


    }
}
