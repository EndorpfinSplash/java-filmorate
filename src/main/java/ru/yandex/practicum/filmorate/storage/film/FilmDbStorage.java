package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        String sql = "select * from FILM order by ID";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    Film film = Film.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("title"))
                            .description(rs.getString("description"))
                            .releaseDate(rs.getDate("release_date").toLocalDate())
                            .duration(rs.getInt("duration"))
                            .mpa(Mpa.builder().id(rs.getInt("mpa_id")).build())
                            .build();

//                    film.getLikes().
//                            film.getGenres()

                    return film;
                });
    }

    @Override
    public Film createFilm(Film film) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("TITLE", film.getName());
        parameters.put("DESCRIPTION", film.getDescription());
        parameters.put("RELEASE_DATE", film.getReleaseDate());
        parameters.put("DURATION", film.getDuration());
        parameters.put("MPA_ID", film.getMpa().getId());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILM")
                .usingGeneratedKeyColumns("ID");

        Integer id = (Integer) simpleJdbcInsert.executeAndReturnKey(parameters);
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int howManyUpdated = jdbcTemplate.update(
                "UPDATE FILM set TITLE=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, MPA_ID=? where ID =?"
                , film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (howManyUpdated == 0) {
            throw new FilmNotFoundException(String.format("Film with id =%d not found", film.getId()));
        }
        return film;
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
//            Set<Integer> filmGenres = film.getGenres();
// TODO: film likes
            return Optional.of(film);
        } else {
            log.info("фильм с идентификатором {} не найден.", filmId);
            return Optional.empty();
        }
    }
}
