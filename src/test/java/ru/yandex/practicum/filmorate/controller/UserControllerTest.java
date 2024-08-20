package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserControllerTest {
    UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void create() {
        User user = createUser();
        controller.create(user);

        assertTrue(controller.getUsers().contains(user));
    }

    @Test
    void create_whenLoginWithSpaces_thenThrowValidationException() {
        User user = createUser();
        user.setLogin("User With Spaces");

        assertThrows(ValidationException.class, () -> controller.create(user));
    }

    @Test
    void update() {
        User user = createUser();
        User oldUser = controller.create(user);
        User newUser = createUser();
        newUser.setId(oldUser.getId());
        newUser.setName("newName");
        newUser.setEmail("newEmail@qwe.com");
        newUser.setLogin("newLogin");
        newUser.setBirthday(LocalDate.of(1999, 12, 23));
        controller.update(newUser);

        assertEquals(oldUser, newUser);
    }

    @Test
    void update_whenUserWithoutId_thenThrowValidationException() {
        User user = createUser();

        assertThrows(ValidationException.class, () -> controller.update(user));
    }

    @Test
    void update_whenUserNotFound_thenThrowValidationException() {
        User user = createUser();
        user.setId(999L);

        assertThrows(NotFoundException.class, () -> controller.update(user));
    }

    @Test
    void update_whenSomeFieldsNullable_thenReturnOldUserFields() {
        User user = createUser();
        controller.create(user);
        User newUser = new User();
        newUser.setId(user.getId());

        User updatedUser = controller.update(newUser);

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