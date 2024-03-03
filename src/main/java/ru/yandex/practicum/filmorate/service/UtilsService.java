package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.utils.UtilsDbStorage;

import java.util.Collection;

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


    public Mpa getMpaById(Integer id) {
        return utilsDbStorage.getMpaById(id);
    }

    public Genre getGenreById(Integer id) {
        return utilsDbStorage.getGenreById(id);
    }

}
