package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.UtilsService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {

    private final UtilsService utilsService;

    @Autowired
    public MpaController(UtilsService utilsService) {
        this.utilsService = utilsService;
    }

    @GetMapping
    public Collection<Mpa> getAllMpa() {
        log.info("GET request to fetch collection of MPA received.");
        return utilsService.getAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable("id") Integer id) {
        log.info("GET request to fetch mpa_id={} received.", id);
        return utilsService.getMpaById(id);
    }


}
