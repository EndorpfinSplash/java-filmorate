package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectAbsentException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    public static final LocalDate OLDEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    static int idFilmCounter = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        film.setId(idFilmCounter);

        if (films.containsKey(film.getId())) {
            throw new ObjectAbsentException("Film already exists");
        }

        validate(film);
        films.put(film.getId(), film);
        idFilmCounter++;
        log.info("Film was created");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            validate(film);
            films.put(filmId, film);
            log.info("Film was updated");
            return film;
        }
        log.info("This film absent");
        throw new ObjectAbsentException("This film absent");
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(OLDEST_RELEASE_DATE)) {
            throw new ValidationException("Couldn't be earlier " + OLDEST_RELEASE_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
