package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("ValidationException " + film);
            throw new ValidationException("Film date must be after 28.12.1895");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("new film created: " + film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (film.getId() == null) {
            log.warn("ValidationException " + film);
            throw new ValidationException("id should be specified");
        }
        if (!films.containsKey(film.getId())) {
            log.info("NotFoundException " + film);
            throw new NotFoundException("film not found");
        }
        Film oldFilm = films.get(film.getId());
        //update parameters for old film if not null
        if (film.getName() != null && !film.getName().isBlank()) {
            oldFilm.setName(film.getName());
        }
        if(film.getDuration() != 0){
            oldFilm.setDuration(film.getDuration());
        }
        oldFilm.setReleaseDate(Objects.requireNonNullElse(film.getReleaseDate(), oldFilm.getReleaseDate()));
        oldFilm.setDescription(Objects.requireNonNullElse(film.getDescription(), oldFilm.getDescription()));
        log.debug("film with id " + oldFilm.getId() + " changed to:" + oldFilm);
        return oldFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
