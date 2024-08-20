package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public Collection<Film> getFilms() {
        return storage.getAllFilms();
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("ValidationException " + film);
            throw new ValidationException("Film date must be after 28.12.1895");
        }
        storage.addFilm(film);
        log.debug("new film created: " + film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            log.warn("ValidationException " + film);
            throw new ValidationException("id should be specified");
        }
        Film oldFilm = storage.getFilm(film.getId());
        if (oldFilm == null) {
            log.info("NotFoundException " + film);
            throw new NotFoundException("film not found");
        }
        //update parameters for old film if not null
        if (film.getName() != null && !film.getName().isBlank()) {
            oldFilm.setName(film.getName());
        }
        if (film.getDuration() != null) {
            oldFilm.setDuration(film.getDuration());
        }
        oldFilm.setReleaseDate(Objects.requireNonNullElse(film.getReleaseDate(), oldFilm.getReleaseDate()));
        oldFilm.setDescription(Objects.requireNonNullElse(film.getDescription(), oldFilm.getDescription()));
        log.debug("film with id " + oldFilm.getId() + " changed to:" + oldFilm);
        return oldFilm;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = storage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
        if (userService.isUserNotExist(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = storage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
        if (userService.isUserNotExist(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> findFilmsByLikes(Integer count) {
        List<Film> films = storage.getAllFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size()))
                .toList()
                .reversed();
        if (count > films.size()) {
            count = films.size();
        }
        return films.subList(0, count);
    }
}