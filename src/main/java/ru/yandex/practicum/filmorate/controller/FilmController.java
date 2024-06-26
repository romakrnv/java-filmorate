package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
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
import java.util.Optional;

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
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Maximum description length is 200");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film date must be after 28.12.1895");
        }
        if (film.getDuration().isZero() || film.getDuration().isNegative()) {
            throw new ValidationException("Duration must be greater than zero");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (film.getId() == null) {
            throw new ValidationException("id should be specified");
        }
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("film not found");
        }
        Film oldFilm = films.get(film.getId());
        //update parameters for old film if not null
        if (film.getName() != null && !film.getName().isBlank()) {
            oldFilm.setName(film.getName());
        }
        oldFilm.setDuration(Objects.requireNonNullElse(film.getDuration(), oldFilm.getDuration()));
        oldFilm.setReleaseDate(Objects.requireNonNullElse(film.getReleaseDate(), oldFilm.getReleaseDate()));
        oldFilm.setDescription(Objects.requireNonNullElse(film.getDescription(), oldFilm.getDescription()));
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
