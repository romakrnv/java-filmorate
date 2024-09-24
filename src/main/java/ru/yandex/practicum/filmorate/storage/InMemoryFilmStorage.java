package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> storage = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return storage.values();
    }

    @Override
    public Film getFilm(Long id) {
        return storage.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        return null;
    }

    @Override
    public Collection<Genre> findAllGenres() {
        return null;
    }

    @Override
    public Genre findGenreById(Long id) {
        return null;
    }

    @Override
    public Collection<Mpa> findAllMpa() {
        return null;
    }

    @Override
    public Mpa findMpaById(Long id) {
        return null;
    }

    @Override
    public void filmAddLike(Long filmId, Long userId) {

    }

    @Override
    public void filmRemoveLike(Long filmId, Long userId) {

    }

    private long getNextId() {
        long currentMaxId = storage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
