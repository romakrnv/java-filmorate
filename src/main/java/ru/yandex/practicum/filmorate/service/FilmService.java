package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;

    public Collection<Film> getFilms() {
        return storage.getAllFilms();
    }

    public Film getFilm(Long id) {
        return storage.getFilm(id);
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("ValidationException " + film);
            throw new ValidationException("Film date must be after 28.12.1895");
        }
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.update(film);
    }

    public void addLike(Long filmId, Long userId) {
        storage.filmAddLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        storage.filmRemoveLike(filmId, userId);
    }

    public List<Film> findFilmsByLikes(Integer count) {
        return storage.getAllFilms().stream()
                .sorted((f1, f2) -> (f2.getLikes().size() - f1.getLikes().size()))
                .limit(count)
                .toList();
    }

    public Genre getGenre(Long id) {
        return storage.findGenreById(id);
    }

    public Collection<Genre> getAllGenres() {
        return storage.findAllGenres();
    }

    public Mpa getMpa(Long id) {
        return storage.findMpaById(id);
    }

    public Collection<Mpa> getAllMpa() {
        return storage.findAllMpa();
    }
}
