package ru.yandex.practicum.filmorate.storage.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;


@Component
public class UtilsDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UtilsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Mpa> getAllMpa() {
        return null;
    }

    public Collection<Genre> getAllGenres() {
        return null;
    }


    public Mpa getMpaById(Integer id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from MPA_DICTIONARY where id = ?", id);
        Mpa mpa = new Mpa();
        mpa.setId(id);
        return mpa;
    }

    public Genre getGenreById(Integer id) {
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select * from GENRE_DICTIONARY where id = ?", id);
        Genre genre = new Genre();
        genre.setId(id);
        return genre;
    }
}
