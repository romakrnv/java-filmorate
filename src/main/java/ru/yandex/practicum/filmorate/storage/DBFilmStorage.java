package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;

import static ru.yandex.practicum.filmorate.storage.queries.FilmSqlQueries.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
@Primary
public class DBFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAllFilms() {
        return jdbcTemplate.query(FIND_ALL_FILMS_QUERY, new FilmRowMapper());
    }

    @Override
    public Film getFilm(Long id) {
        try {
            return jdbcTemplate.queryForObject(FIND_FILM_BY_ID_QUERY, new FilmRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Film with id: " + id + " not found");
        }
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_FILM_QUERY, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        Long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        Set<Genre> filmGenres = film.getGenres();
        if (filmGenres != null) {
            filmGenres.forEach(genre -> jdbcTemplate.update(INSERT_GENRE_QUERY, filmId, genre.getId()));
        }
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        getFilm(newFilm.getId()); /// убрать
        jdbcTemplate.update(UPDATE_FILM_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                Date.valueOf(newFilm.getReleaseDate()),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        return newFilm;
    }

    @Override
    public Collection<Genre> findAllGenres() {
        return jdbcTemplate.query(FIND_ALL_GENRES_QUERY, new GenreRowMapper());
    }

    @Override
    public Genre findGenreById(Long id) {
        try {
            return jdbcTemplate.queryForObject(FIND_GENRE_BY_ID_QUERY, new GenreRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Genre with id: " + id + " not found");
        }
    }

    @Override
    public Collection<Mpa> findAllMpa() {
        return jdbcTemplate.query(FIND_ALL_MPA_QUERY, new MpaRowMapper());
    }

    @Override
    public Mpa findMpaById(Long id) {
        try {
            return jdbcTemplate.queryForObject(FIND_MPA_BY_ID_QUERY, new MpaRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Mpa with id: " + id + " not found");
        }
    }

    @Override
    public void filmAddLike(Long filmId, Long userId) {
        jdbcTemplate.update(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void filmRemoveLike(Long filmId, Long userId) {
        jdbcTemplate.update(REMOVE_LIKE_QUERY, filmId, userId);
    }
}
