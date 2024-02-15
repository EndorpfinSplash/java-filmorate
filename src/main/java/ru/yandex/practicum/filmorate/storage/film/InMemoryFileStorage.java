package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmAbsentException;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class InMemoryFileStorage
        implements FilmStorage {
    private int idFilmCounter = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();


    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {

        film.setId(idFilmCounter);

        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException(film + " already exists in the list");
        }

        films.put(film.getId(), film);
        idFilmCounter++;
        return film;
    }

    @Override
    public Film update(Film film) {
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            films.put(filmId, film);
            return film;
        }
        throw new FilmAbsentException(film + " absent");
    }
}
