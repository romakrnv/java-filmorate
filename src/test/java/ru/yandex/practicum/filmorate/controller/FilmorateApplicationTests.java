package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DBFilmStorage;
import ru.yandex.practicum.filmorate.storage.DBUserStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
class FilmorateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmorateApplicationTests(DBUserStorage userStorage, DBFilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @Test
    void testFilmCreate() {
        Film film = new Film();

        film.setName("name");
        film.setDescription("description");
        film.setDuration(1000L);
        film.setReleaseDate(LocalDate.of(1991, 2, 10));
        film.setMpa(new Mpa(1L, "R"));

        filmStorage.addFilm(film);
        assertTrue(film.getId() > 0);
    }

    @Test
    public void testFilmFindAll() {
        Collection<Film> all = filmStorage.getAllFilms();
        assertFalse(all.isEmpty());
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = Optional.of(filmStorage.getFilm(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = Optional.of(userStorage.getUser(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testUserFindAll() {
        Collection<User> all = userStorage.getAllUsers();
        assertFalse(all.isEmpty());
    }


    @Test
    public void testUserCreate() {
        User user = new User();

        user.setEmail("test@test");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 11, 11));

        userStorage.addUser(user);
        assertTrue(user.getId() > 0);
    }
}