package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private final FilmService filmService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public Collection<Film> getFilms() {
        log.trace("starting getAllFilms");
        return filmService.getFilms();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/popular")
    public Collection<Film> getFilmsByLikes(@Positive @RequestParam(defaultValue = "10") int count) {
        log.trace("starting GET Popular");
        return filmService.findFilmsByLikes(count);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        log.trace("starting POST Create Film");
        return filmService.addFilm(film);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping()
    public Film update(@RequestBody Film film) {
        log.trace("starting PUT Update Film: " + film);
        return filmService.updateFilm(film);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.trace("starting PUT addLike for Film id " + filmId + " from User id + " + userId);
        filmService.addLike(filmId, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.trace("starting Delete removeLike for Film id  " + filmId + " from User id + " + userId);
        filmService.removeLike(filmId, userId);
    }
}
