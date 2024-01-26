package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        int filmId = film.getId();

        if (films.containsKey(filmId)) {
            throw new UserAlreadyExistException("Film already exists");
        }

        films.put(filmId, film);
        log.info("Film was created");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {

        films.put(film.getId(), film);
        log.info("Film was updated");
        return film;
    }

}
