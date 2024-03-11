package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.utils.UtilsDbStorage;

import java.util.Collection;
import java.util.Set;

@Service
public class UtilsService {

    private final UtilsDbStorage utilsDbStorage;

    @Autowired
    public UtilsService(UtilsDbStorage utilsDbStorage) {
        this.utilsDbStorage = utilsDbStorage;
    }

    public Collection<Mpa> getAllMpa() {
        return utilsDbStorage.getAllMpa();
    }

    public Collection<Genre> getAllGenres() {
        return utilsDbStorage.getAllGenres();
    }


    public Mpa getMpaById(Integer mpaId) {
        return utilsDbStorage.getMpaById(mpaId)
                .orElseThrow(
                        () -> new MpaNotFoundException(String.format("Mpa with id=%d not found", mpaId))
                );
    }

    public Genre getGenreById(Integer genreId) {
        return utilsDbStorage.getGenreById(genreId)
                .orElseThrow(
                        () -> new GenreNotFoundException(String.format("Genre with id=%d not found", genreId))
                );
    }

    public Set<Integer> getUserFriendsId(Integer userId) {
        return utilsDbStorage.getUserFriendsId(userId);
    }

}
