package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/mpa")
public class MpaController {
    private final FilmService filmService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public Collection<Mpa> getAllMpa() {
        return filmService.getAllMpa();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public Mpa getMpaById(@PathVariable Long id) {
        return filmService.getMpa(id);
    }
}
