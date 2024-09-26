package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUser(Long id);

    User addUser(User user);

    User update(User newUser);

    void addFriend(Long id1, Long id2);

    void removeFriend(Long id1, Long id2);
}