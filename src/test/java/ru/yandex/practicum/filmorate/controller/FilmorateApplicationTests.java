package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplicationTests {
    FilmController controller;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
    }

    @Test
    void create() {
        Film film = createFilm();
        controller.create(film);

        assertTrue(controller.getFilms().contains(film));
    }

    @Test
    void create_whenDurationIsMinus_thenThrowValidationException() {
        Film film = createFilm();
        film.setDuration(Duration.ofMinutes(-10));

        assertThrows(ValidationException.class, () -> controller.create(film));
    }

    @Test
    void create_whenDateBefore1895_thenThrowValidationException() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        assertThrows(ValidationException.class, () -> controller.create(film));
    }

    @Test
    void create_whenDescriptionMore200ch_thenThrowValidationException() {
        Film film = createFilm();
        film.setDescription("201ttexttexttexttexttexttexttexttexttexttexttext" +
                "texttexttexttexttexttexttexttexttexttexttexttexttexttexttexttext" +
                "texttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttextt");

        assertThrows(ValidationException.class, () -> controller.create(film));
    }

    @Test
    void update() {
        Film film = createFilm();
        Film oldFilm = controller.create(film);
        Film newFilm = createFilm();
        newFilm.setId(oldFilm.getId());
        newFilm.setDescription("changed description");
        newFilm.setName("new Name");
        newFilm.setDuration(Duration.ofMinutes(30));
        controller.update(newFilm);

        assertEquals(oldFilm, newFilm);
    }

    @Test
    void update_whenFilmWithoutId_thenThrowValidationException() {
        Film film = createFilm();

        assertThrows(ValidationException.class, () -> controller.update(film));
    }

    @Test
    void update_whenFilmNotExist_thenThrowNotFoundException() {
        Film film = createFilm();
        film.setId(999L);

        assertThrows(NotFoundException.class, () -> controller.update(film));
    }

    private Film createFilm() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Some text");
        film.setDuration(Duration.ofMinutes(10));
        film.setReleaseDate(LocalDate.of(2022, 11, 11));
        return film;
    }
}