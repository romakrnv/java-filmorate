package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

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
    public void removeFilm(Long id) {
        storage.remove(id);
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
