package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.UtilsService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/genre")
public class GenreController {

    private final UtilsService utilsService;

    @Autowired
    public GenreController(UtilsService utilsService) {
        this.utilsService = utilsService;
    }

    @GetMapping
    public Collection<Genre> getAllMpa() {
        log.info("GET request to fetch collection of genres received.");
        return utilsService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getMpa(@PathVariable("id") Integer id) {
        log.info("GET request to fetch genre_id={} received.", id);
        return utilsService.getGenreById(id);
    }


}
