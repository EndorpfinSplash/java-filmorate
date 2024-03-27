package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;

    @BeforeAll
    void init() {
        filmDbStorage = new FilmDbStorage(jdbcTemplate);
    }

    @BeforeEach
    void initPreparation() {
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "FILM", "FILM_LIKES");
    }

    @Test
    void test_getAllFilms() {
        final Film testFilm1 = Film.builder()
                .name("Matrix")
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();

        Film savedFilm1 = filmDbStorage.saveFilm(testFilm1);
        final Film testFilm2 = Film.builder()
                .name("Terminator")
                .description("t".repeat(201))
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();
        Film savedFilm2 = filmDbStorage.saveFilm(testFilm2);

        Collection<Film> allFilms = filmDbStorage.getAllFilms();

        Arrays.asList(savedFilm1, savedFilm2).forEach(
                film -> assertTrue(allFilms.contains(film), "List doesn't contain expected film")
        );

        assertEquals(2, allFilms.size(), "List have to contain 2 films");
    }

    @Test
    void test_SaveFilm() {
        final Film testFilm1 = Film.builder()
                .name("Matrix")
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpa(Mpa.builder().id(1).build())
                .build();

        Film savedFilm1 = filmDbStorage.saveFilm(testFilm1);

        assertThat(testFilm1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(savedFilm1);
    }

    @Test
    void test_SaveFilmWithNonUniqueGenres() {
        final Film testFilm1 = Film.builder()
                .name("Matrix")
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpa(Mpa.builder().id(1).build())
                .build();
        testFilm1.getGenres()
                .addAll(List.of(Genre.builder().id(1).build(),
                                Genre.builder().id(2).build(),
                                Genre.builder().id(1).build()
                        )
                );
        Film savedFilm = filmDbStorage.saveFilm(testFilm1);
        assertEquals(2, filmDbStorage.getFilmById(savedFilm.getId()).get().getGenres().size());
    }

    @Test
    void test_updateFilm() {
        final Film testFilm1 = Film.builder()
                .name("Matrix")
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(1999, 3, 31))
                .mpa(Mpa.builder().id(1).build())
                .build();

        Film savedFilm1 = filmDbStorage.saveFilm(testFilm1);

        final Film updatedFilm = Film.builder()
                .id(savedFilm1.getId())
                .name("Matrix Revolution")
                .description("very Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2003, 11, 5))
                .mpa(Mpa.builder().id(2).build())
                .build();

        Optional<Film> filmAfterUpdate = filmDbStorage.updateFilm(updatedFilm);

        assertThat(updatedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmAfterUpdate.get());
    }

    @Test
    void test_getFilmById() {
        final Film testFilm1 = Film.builder()
                .name("Matrix")
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();

        Film savedFilm1 = filmDbStorage.saveFilm(testFilm1);

        Optional<Film> gettedByIdFilm = filmDbStorage.getFilmById(savedFilm1.getId());

        assertThat(savedFilm1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(gettedByIdFilm.get());
    }

    @Test
    void addLike() {
        final Film testFilm1 = Film.builder()
                .name("Matrix")
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film savedTestFilm1 = filmDbStorage.saveFilm(testFilm1);

        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User testUser1 = User.builder()
                .login("testUser1")
                .name("John Petrov")
                .email("user1@email.us")
                .birthday(LocalDate.of(1970, 1, 1))
                .build();
        User savedTestUser = userDbStorage.saveUser(testUser1);

        filmDbStorage.addLike(savedTestFilm1.getId(), savedTestUser.getId());

        Set<Integer> filmLikes = filmDbStorage.getFilmById(savedTestFilm1.getId()).get().getLikes();
        assertTrue(filmLikes.contains(savedTestUser.getId()));
        assertEquals(1, filmLikes.size());
    }

    @AfterAll
    void tearDown() {
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "FILM", "FILM_LIKES");
    }
}