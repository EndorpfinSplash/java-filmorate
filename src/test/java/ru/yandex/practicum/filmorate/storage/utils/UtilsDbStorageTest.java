package ru.yandex.practicum.filmorate.storage.utils;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UtilsDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private UtilsDbStorage utilsDbStorage;

    @BeforeAll
    void init() {
        this.utilsDbStorage = new UtilsDbStorage(jdbcTemplate);
    }

    @Test
    void getAllMpa() {
        List<String> mpa_titles = utilsDbStorage.getAllMpa().stream()
                .map(Mpa::getName)
                .collect(Collectors.toList());

        Arrays.asList("G", "PG", "PG-13", "R", "NC-17").forEach(
                mpa -> assertTrue(mpa_titles.contains(mpa)));
    }

    @Test
    void getAllGenres() {
        List<String> genres_names = utilsDbStorage.getAllGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toList());

        Arrays.asList("Боевик",
                "Документальный",
                "Драма",
                "Комедия",
                "Мультфильм",
                "Триллер").forEach(
                genre -> assertTrue(genres_names.contains(genre))
        );
    }

    @Test
    void getMpaById() {
        Mpa expectedMpa = Mpa.builder()
                .id(1)
                .name("G")
                .description("у фильма нет возрастных ограничений")
                .build();
        Mpa gettedMpa = utilsDbStorage.getMpaById(1).get();
        assertThat(expectedMpa)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(gettedMpa);
    }

    @Test
    void getGenreById() {
        Genre expectedGenre = Genre.builder()
                .id(6)
                .name("Боевик")
                .build();
        Genre gettedGenre = utilsDbStorage.getGenreById(6).get();
        assertThat(expectedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(gettedGenre);
    }
}