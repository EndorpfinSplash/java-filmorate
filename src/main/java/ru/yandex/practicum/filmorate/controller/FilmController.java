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

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("GET request to fetch collection of films received.");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Integer filmId) {
        log.info("GET request to fetch film_id={} received.", filmId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        log.info("GET top of {} films", count);
        return filmService.getTopFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("POST request to create {} received.", film);
        validateFilm(film);
        Film createdFilm = filmService.createFilm(film);
        log.info(film + " was created");
        return createdFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT request to update {} received.", film);
        validateFilm(film);
        log.info(film + " was updated");
        return filmService.updateFilm(film);
    }


    @PutMapping("/{id}/like/{userId}")
    public Film setLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("PUT request to set like from user_id={} for film_id={}.", userId, filmId);
        Film film = filmService.setLikeForFilm(filmId, userId);
        log.info("Like from user_id={} for film_id={} was set", userId, filmId);
        return film;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("PUT request to set like from user_id={} for film_id={}.", userId, filmId);

        log.info("Like from user_id={} for film_id={} was set", userId, filmId);
        return filmService.deleteLikeFromFilm(filmId, userId);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(OLDEST_RELEASE_DATE)) {
            throw new ValidationException(String.format("Couldn't be earlier %s", OLDEST_RELEASE_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE)));
        }
    }
}
