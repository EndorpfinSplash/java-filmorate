package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {

    @Qualifier("inMemoryFilmStorage")
    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        Film filmForLike = filmStorage.getFilm(filmId);
        filmForLike.getLikes().add(userId);
        return filmForLike;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film filmForLike = getFilm(filmId);
        userStorage.getUser(userId);
        filmForLike.getLikes().remove(userId);
        return filmForLike;
    }

    public List<Film> getTopFilms(Integer count) {
        TreeSet<Film> filmRating = new TreeSet<>(Comparator.comparing(film -> film.getLikes().size(), (o1, o2) -> o2 - o1));
        filmRating.addAll(filmStorage.getAll());
        if (count > filmRating.size()) {
            return new ArrayList<>(filmRating);
        }
        return new ArrayList<>(filmRating).subList(0, count);
    }
}
