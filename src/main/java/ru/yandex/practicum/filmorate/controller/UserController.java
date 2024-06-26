package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private static final String regex = "^[\\w-]+@([\\w-]+\\.)+[\\w-]+$";


    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("login cannot contains spaces or be empty");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() == null) {
            throw new ValidationException("id should be specified");
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("user not found");
        }

        User oldUser = users.get(user.getId());
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
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
