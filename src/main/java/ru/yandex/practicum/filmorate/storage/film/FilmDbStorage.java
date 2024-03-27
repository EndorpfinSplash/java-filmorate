package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

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
                            .build();
                    film.getLikes().addAll(getFilmLikes(filmId));
                    film.getGenres().addAll(getFilmGenres(filmId));
                    Optional<Mpa> mpaOptional = getMpaById(rs.getInt("mpa_id"));
                    mpaOptional.ifPresent(film::setMpa);
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
            Integer mpaId = mpa.getId();
            validateMpa(mpaId);
            parameters.put("MPA_ID", mpaId);
        }
        Integer savedFilmId = (Integer) simpleJdbcInsert.executeAndReturnKey(parameters);
        film.getGenres().forEach(
                genre -> {
                    validateGenre(genre.getId());
                    setGenreForFilm(savedFilmId, genre.getId());
                });

        film.setId(savedFilmId);
        return film;
    }

    private void validateMpa(Integer mpaId) {
        String sql = "select count(*) from MPA_DICTIONARY where ID = ?";
        Integer cntMpaIds = jdbcTemplate.queryForObject(sql, Integer.class, mpaId);
        if (cntMpaIds == null || cntMpaIds != 1) {
            throw new ValidationException(MessageFormat.format("Mpa with id={0} doesn't exist", mpaId));
        }
    }

    private void validateGenre(Integer genreId) {
        String sql = "select count(*) from GENRE_DICTIONARY where ID = ?";
        Integer cntMpaIds = jdbcTemplate.queryForObject(sql, Integer.class, genreId);
        if (cntMpaIds == null || cntMpaIds != 1) {
            throw new ValidationException(MessageFormat.format("Genre with id={0} doesn't exist", genreId));
        }
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
                    .build();
            film.getLikes().addAll(getFilmLikes(filmId));
            film.getGenres().addAll(getFilmGenres(filmId));
            Optional<Mpa> mpaOptional = getMpaById(filmRows.getInt("mpa_id"));
            mpaOptional.ifPresent(film::setMpa);
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


    public boolean setGenreForFilm(Integer filmId, Integer genreId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILM_GENRE");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("FILM_ID", filmId);
        parameters.put("GENRE_ID", genreId);
        int execute = 0;
        try {
            execute = simpleJdbcInsert.execute(parameters);
        } catch (DuplicateKeyException e) {
            execute = 0;
        }
        return execute == 1;
    }

    private Collection<Integer> getFilmLikes(Integer filmId) {
        String sql = "select USER_ID from FILM_LIKES where FILM_ID = ?";
        List<Integer> likers = jdbcTemplate.queryForList(sql, Integer.class, filmId);
        return new HashSet<>(likers);
    }

    private Collection<Genre> getFilmGenres(Integer filmId) {
        List<Integer> genreIds = jdbcTemplate.queryForList("select GENRE_ID from FILM_GENRE where FILM_ID = ?",
                Integer.class,
                filmId);
        if (genreIds.isEmpty()) {
            return Collections.emptyList();
        }
        return genreIds.stream()
                .map(genreId -> getGenreById(genreId).get())
                .collect(Collectors.toList());

    }

    private Optional<Genre> getGenreById(Integer genreId) {
        try {
            Genre genre = jdbcTemplate.queryForObject(
                    "SELECT * FROM GENRE_DICTIONARY where id = ?",
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

    public Optional<Mpa> getMpaById(Integer mpaId) {
        try {
            Mpa mpa = jdbcTemplate.queryForObject(
                    "select * from MPA_DICTIONARY where id = ?",
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

}
