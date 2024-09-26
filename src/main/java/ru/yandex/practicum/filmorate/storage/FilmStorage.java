package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film getFilm(Long id);

    Film addFilm(Film film);

    Film update(Film newFilm);

    Collection<Genre> findAllGenres();

    Genre findGenreById(Long id);

    Collection<Mpa> findAllMpa();

    Mpa findMpaById(Long id);

    void filmAddLike(Long filmId, Long userId);

    void filmRemoveLike(Long filmId, Long userId);
}
