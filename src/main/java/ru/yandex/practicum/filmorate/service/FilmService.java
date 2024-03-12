package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(
                        () -> new FilmNotFoundException(MessageFormat.format("Film with id={} not found", filmId))
                );
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film setLikeForFilm(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(
                        () -> new FilmNotFoundException(MessageFormat.format("Film with id={} not found", filmId))
                );
        userStorage.getUserById(userId);
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLikeFromFilm(Integer filmId, Integer userId) {
        Film filmForLike = filmStorage.getFilmById(filmId).orElseThrow(
                () -> new FilmNotFoundException(MessageFormat.format("Film with id={} not found", filmId)));
        userStorage.getUserById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with id={} not found", userId)));
        filmForLike.getLikes().remove(userId);
        return filmForLike;
    }

    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(comparingInt(film -> -1 * film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
