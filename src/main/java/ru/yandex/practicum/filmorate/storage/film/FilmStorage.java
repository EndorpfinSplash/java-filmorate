package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;


public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<Film> getFilmById(Integer id);

    boolean addLike(Integer filmId, Integer userId);
}
