package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserStorage storage;

    public Collection<User> getUsers() {
        return storage.getAllUsers();
    }

    public void addFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            return; //or new throw?
        }
        User user1 = storage.getUser(id);
        if (user1.getFriends().contains(storage.getUser(friendId).getId())) {
            return;
        }
        storage.addFriend(id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        storage.getUser(id);
        storage.getUser(friendId);
        storage.removeFriend(id, friendId);
    }

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        return storage.update(user);
    }

    public Collection<User> getFriends(Long id) {
        User user = storage.getUser(id);
        return user.getFriends().stream().map(storage::getUser).toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        User user1 = storage.getUser(userId);
        User user2 = storage.getUser(otherId);

        Set<Long> resultSet = new HashSet<>(user1.getFriends());
        resultSet.retainAll(user2.getFriends());

        return resultSet.stream().map(storage::getUser).toList();
    }
}
