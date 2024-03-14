package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "select * from FILM";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    int filmId = rs.getInt("id");
                    Film film = Film.builder()
                            .id(filmId)
                            .name(rs.getString("title"))
                            .description(rs.getString("description"))
                            .releaseDate(rs.getDate("release_date").toLocalDate())
                            .duration(rs.getInt("duration"))
                            .mpa(Mpa.builder().id(rs.getInt("mpa_id")).build())
                            .build();
                    film.getLikes().addAll(getfilmLikes(filmId));
//                            film.getGenres()
                    return film;
                });
    }

    @Override
    public Film saveFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILM")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", film.getId());
        parameters.put("TITLE", film.getName());
        parameters.put("DESCRIPTION", film.getDescription());
        parameters.put("RELEASE_DATE", film.getReleaseDate());
        parameters.put("DURATION", film.getDuration());
        Mpa mpa = film.getMpa();
        if (mpa != null) {
            parameters.put("MPA_ID", mpa.getId());
        }

        Integer id = (Integer) simpleJdbcInsert.executeAndReturnKey(parameters);
        film.setId(id);
        return film;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        int updated = jdbcTemplate.update(
                "UPDATE FILM set TITLE =?, DESCRIPTION =?, RELEASE_DATE=?, DURATION = ? where ID = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());
        return updated == 1 ? Optional.of(film) : Optional.empty();
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM where id = ?", filmId);

        if (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getInt("id"))
                    .name(filmRows.getString("title"))
                    .description(filmRows.getString("description"))
                    .releaseDate(filmRows.getDate("release_date").toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .mpa(Mpa.builder().id(filmRows.getInt("mpa_id")).build())
                    .build();
            film.getLikes().addAll(getfilmLikes(filmId));

            return Optional.of(film);
        } else {
            log.info("фильм с идентификатором {} не найден.", filmId);
            return Optional.empty();
        }
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILM_LIKES");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("FILM_ID", filmId);
        parameters.put("USER_ID", userId);
        int execute = simpleJdbcInsert.execute(parameters);
        return execute == 1;
    }

    private Collection<Integer> getfilmLikes(Integer filmId) {
        String sql = "select USER_ID from FILM_LIKES where FILM_ID = ?";
        List<Integer> likers = jdbcTemplate.queryForList(sql, Integer.class, filmId);
        return new HashSet<>(likers);
    }

}
