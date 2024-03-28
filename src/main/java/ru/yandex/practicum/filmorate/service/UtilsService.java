package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.utils.UtilsDbStorage;

import java.text.MessageFormat;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UtilsService {

    private final UtilsDbStorage utilsDbStorage;

    public Collection<Mpa> getAllMpa() {
        return utilsDbStorage.getAllMpa();
    }

    public Collection<Genre> getAllGenres() {
        return utilsDbStorage.getAllGenres();
    }


    public Mpa getMpaById(Integer mpaId) {
        return utilsDbStorage.getMpaById(mpaId)
                .orElseThrow(
                        () -> new MpaNotFoundException(MessageFormat.format("Mpa with id={0} not found", mpaId))
                );
    }

    public Genre getGenreById(Integer genreId) {
        return utilsDbStorage.getGenreById(genreId)
                .orElseThrow(
                        () -> new GenreNotFoundException(MessageFormat.format("Genre with id={0} not found", genreId))
                );
    }


}
