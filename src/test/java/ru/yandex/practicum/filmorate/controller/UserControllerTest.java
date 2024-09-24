package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserControllerTest {
    UserStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
    }

    @Test
    void create() {
        User user = createUser();
        userStorage.addUser(user);

        assertTrue(userStorage.getAllUsers().contains(user));
    }

    @Test
    void update() {
        User user = createUser();
        User oldUser = userStorage.addUser(user);
        User newUser = createUser();
        newUser.setId(oldUser.getId());
        newUser.setName("newName");
        newUser.setEmail("newEmail@qwe.com");
        newUser.setLogin("newLogin");
        newUser.setBirthday(LocalDate.of(1999, 12, 23));

        assertEquals(oldUser, userStorage.update(newUser));
    }


    @Test
    void update_whenSomeFieldsNullable_thenReturnOldUserFields() {
        User user = createUser();
        userStorage.addUser(user);
        User newUser = new User();
        newUser.setId(user.getId());

        User updatedUser = userStorage.update(newUser);

        assertEquals("UserName", updatedUser.getName());
        assertEquals("test@test.com", updatedUser.getEmail());
        assertEquals("UserLogin", updatedUser.getLogin());
        assertEquals(LocalDate.of(2010, 2, 13), updatedUser.getBirthday());
    }

    private User createUser() {
        User user = new User();
        user.setName("UserName");
        user.setEmail("test@test.com");
        user.setLogin("UserLogin");
        user.setBirthday(LocalDate.of(2010, 2, 13));
        return user;
    }
}