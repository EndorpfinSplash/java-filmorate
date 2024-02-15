package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAbsentException;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.Film.OLDEST_RELEASE_DATE;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    private int idFilmCounter = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("GET request to fetch list of films received.");
        return new ArrayList(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("POST request to create " + film + " received.");
        film.setId(idFilmCounter);

        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException(film + " already exists in the list");
        }

        validateFilm(film);
        films.put(film.getId(), film);
        idFilmCounter++;
        log.info(film + " was created");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT request to update " + film + " received.");
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            validateFilm(film);
            films.put(filmId, film);
            log.info(film + " was updated");
            return film;
        }
        log.info(film + " absent in list");
        throw new FilmAbsentException(film + " absent");
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(OLDEST_RELEASE_DATE)) {
            throw new ValidationException("Couldn't be earlier " + OLDEST_RELEASE_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
