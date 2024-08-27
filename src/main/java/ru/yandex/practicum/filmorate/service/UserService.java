package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserStorage storage;
    private static final String regex = "^[\\w-]+@([\\w-]+\\.)+[\\w-]+$";

    public Collection<User> getUsers() {
        return storage.getAllUsers();
    }

    public User addUser(User user) {
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.warn("ValidationException " + user);
            throw new ValidationException("login cannot contains spaces or be empty");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        storage.addUser(user);
        log.warn("test trace" + user);
        log.debug("new user created: " + user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == null) {
            log.warn("ValidationException " + user);
            throw new ValidationException("id should be specified");
        }
        User oldUser = getUserOrThrow(user.getId());
        //update parameters for old user if not null
        if (user.getLogin() != null && !user.getLogin().isBlank() && !user.getLogin().contains(" ")) {
            oldUser.setLogin(user.getLogin());
        }
        oldUser.setName(Objects.requireNonNullElse(user.getName(), oldUser.getName()));
        if (user.getEmail() != null && !user.getEmail().isBlank() && user.getEmail().matches(regex)) {
            oldUser.setEmail(user.getEmail());
        }
        if (user.getBirthday() != null && user.getBirthday().isBefore(LocalDate.now())) {
            oldUser.setBirthday(user.getBirthday());
        }
        log.debug("user with id " + oldUser.getId() + " was updated to : " + oldUser);
        return oldUser;
    }

    public void addFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("The same ID were specified");
        }
        User user = getUserOrThrow(id);
        User userFriend = getUserOrThrow(friendId);
        user.getFriends().add(userFriend.getId());
        userFriend.getFriends().add(user.getId());
    }

    public void removeFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("The same ID were specified");
        }
        User user = getUserOrThrow(id);
        User userFriend = getUserOrThrow(friendId);
        user.getFriends().remove(userFriend.getId());
        userFriend.getFriends().remove(user.getId());
    }

    public Collection<User> getFriends(Long id) {
        User user = getUserOrThrow(id);
        return user.getFriends().stream()
                .map(storage::getUser)
                .toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        if (userId.equals(otherId)) {
            throw new ValidationException("The same ID were specified");
        }
        User user = getUserOrThrow(userId);
        User anotherUser = getUserOrThrow(otherId);
        return user.getFriends().stream()
                .filter(anotherUser.getFriends()::contains)
                .map(storage::getUser)
                .toList();
    }

    public boolean isUserNotExist(Long id) {
        return storage.getUser(id) == null;
    }

    private User getUserOrThrow(Long id) {
        User user = storage.getUser(id);
        if (user == null) {
            log.info("NotFoundException user with id: " + id);
            throw new NotFoundException("user with id " + id + " not found");
        }
        return user;
    }
}
