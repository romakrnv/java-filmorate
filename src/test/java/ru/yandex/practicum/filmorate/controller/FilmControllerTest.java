package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
    }

    @Test
    void create() {
        Film film = createFilm();
        filmStorage.addFilm(film);

        assertTrue(filmStorage.getAllFilms().contains(film));
    }

    @Test
    void update() {
        Film film = createFilm();
        filmStorage.addFilm(film);
        Film newFilm = new Film();
        newFilm.setId(film.getId());

        Film updatedFilm = filmStorage.update(newFilm);

        assertEquals("FilmName", updatedFilm.getName());
        assertEquals("text", updatedFilm.getDescription());
        assertEquals(1000L, updatedFilm.getDuration());
        assertEquals(LocalDate.of(2010, 2, 13), updatedFilm.getReleaseDate());
    }

    private Film createFilm() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("text");
        film.setDuration(1000L);
        film.setReleaseDate(LocalDate.of(2010, 2, 13));
        return film;
    }
}