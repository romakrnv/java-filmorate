package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return storage.values();
    }

    @Override
    public User getUser(Long id) {
        return storage.get(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        return storage.put(newUser.getId(), newUser);
    }

    @Override
    public void addFriend(Long id1, Long id2) {

    }

    @Override
    public void removeFriend(Long id1, Long id2) {

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
