package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("ID"));
        film.setName(resultSet.getString("NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setReleaseDate(resultSet.getDate("RELEASEDATE").toLocalDate());
        film.setDuration(resultSet.getLong("DURATION"));
        film.setMpa(new Mpa(resultSet.getLong("RATING_ID"),
                resultSet.getString("RATING_NAME"))); ////////////

        String genres = resultSet.getString("GENRES");
        Set<Genre> genreHashSet = new HashSet<>();
        if (genres != null) {
            String[] splitGenres = genres.split(",");
            for (String strGenre : splitGenres) {
                String[] genreRow = strGenre.split("/");
                Genre g = new Genre(Long.parseLong(genreRow[0]), genreRow[1]);
                genreHashSet.add(g);
            }
            film.setGenres(genreHashSet);
        }

        String likes = resultSet.getString("LIKES");
        if (likes != null) {
            film.setLikes(Arrays.stream(likes.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toSet()));
        }
        return film;
    }
}
