package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;
    private static final String regex = "^[\\w-]+@([\\w-]+\\.)+[\\w-]+$";

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

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
        User oldUser = storage.getUser(user.getId());
        if (oldUser == null) {
            log.info("NotFoundException " + user);
            throw new NotFoundException("user not found");
        }

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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("The same ID were specified");
        }
        User user = storage.getUser(id);
        User userFriend = storage.getUser(friendId);
        if (user == null || userFriend == null) {
            throw new NotFoundException("User not found");
        }
        user.getFriends().add(userFriend.getId());
        userFriend.getFriends().add(user.getId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("The same ID were specified");
        }
        User user = storage.getUser(id);
        User userFriend = storage.getUser(friendId);
        if (user == null || userFriend == null) {
            throw new NotFoundException("User not found");
        }
        user.getFriends().remove(userFriend.getId());
        userFriend.getFriends().remove(user.getId());
    }

    public Collection<User> getFriends(Long id) {
        User user = storage.getUser(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user.getFriends().stream()
                .map(storage::getUser)
                .toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        if (userId.equals(otherId)) {
            throw new ValidationException("The same ID were specified");
        }
        User user = storage.getUser(userId);
        User anotherUser = storage.getUser(otherId);
        if (user == null || anotherUser == null) {
            throw new NotFoundException("User not found");
        }
        return user.getFriends().stream()
                .filter(anotherUser.getFriends()::contains)
                .map(storage::getUser)
                .toList();
    }

    public boolean isUserNotExist(Long id) {
        return storage.getUser(id) == null;
    }
}
