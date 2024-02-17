package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {

    @Qualifier("inMemoryFilmStorage")
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film setLike(Integer filmId, Integer userId) {
        Film filmForLike = getFilm(filmId);
        filmForLike.getLikes().add(userId);
        return filmForLike;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film filmForLike = getFilm(filmId);
        filmForLike.getLikes().remove(userId);
        return filmForLike;
    }

    public List<Film> getTopFilms(Integer count) {
        return null;
    }
}
