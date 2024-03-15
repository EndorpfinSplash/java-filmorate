package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;

    @BeforeEach
    void initPreparation() {
        filmDbStorage = new FilmDbStorage(jdbcTemplate);
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "FILM", "FILM_LIKES");
    }

    @Test
    void getAllFilms() {
        final Film testFilm1 = Film.builder()
                .name("Matrix")
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpa(Mpa.builder().id(1).build())
                .build();

        Film savedFilm1 = filmDbStorage.saveFilm(testFilm1);
        final Film testFilm2 = Film.builder()
                .name("Terminator")
                .description("t".repeat(201))
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film savedFilm2 = filmDbStorage.saveFilm(testFilm2);

        Collection<Film> allFilms = filmDbStorage.getAllFilms();

        Arrays.asList(savedFilm1, savedFilm2).forEach(
                film -> assertTrue(allFilms.contains(film), "List doesn't contain expected film")
        );

        assertEquals(2, allFilms.size(), "List have to contain 2 films");
    }

    @Test
    void saveFilm() {
    }

    @Test
    void updateFilm() {
    }

    @Test
    void getFilmById() {
    }

    @Test
    void addLike() {
    }
}