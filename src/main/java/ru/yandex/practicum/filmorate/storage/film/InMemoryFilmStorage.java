package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmAbsentException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private int idFilmCounter = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(idFilmCounter);
        films.put(film.getId(), film);
        idFilmCounter++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            films.put(filmId, film);
            return film;
        }
        throw new FilmAbsentException(film + " absent");
    }

    @Override
    public Film getFilmById(Integer id) {
        return films.computeIfAbsent(id, integer -> {
            throw new FilmNotFoundException(String.format("Film with id=%s absent", id));
        });
    }
}
