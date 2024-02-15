package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

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

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("GET request to fetch collection of films received.");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Integer id) {
        log.info("GET request to fetch film received.");
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("POST request to create {} received.", film);
        validateFilm(film);
        Film createdFilm = filmService.create(film);
        log.info(film + " was created");
        return createdFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT request to update {} received.", film);
        validateFilm(film);
        log.info(film + " was updated");
        return filmService.update(film);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(OLDEST_RELEASE_DATE)) {
            throw new ValidationException("Couldn't be earlier " + OLDEST_RELEASE_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
