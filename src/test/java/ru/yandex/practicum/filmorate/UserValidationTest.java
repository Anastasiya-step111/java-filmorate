package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserValidationTest {

    @Autowired
    private UserController userController;

    @Test
    void shouldNotCreateUserWithBlankEmail() {
        User user = new User();
        user.setEmail("");
        user.setLogin("valid_login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldNotCreateUserWithNullEmail() {
        User user = new User();
        user.setEmail(null);
        user.setLogin("valid_login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldNotCreateUserWithEmailWithoutAt() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("valid_login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldCreateUserWithValidEmail() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("valid_login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertDoesNotThrow(() -> userController.create(user));
    }

    @Test
    void shouldNotCreateUserWithBlankLogin() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldNotCreateUserWithLoginWithSpace() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("login with space");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldNotCreateUserWithFutureBirthday() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("valid_login");
        user.setBirthday(LocalDate.of(2030, 1, 1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldCreateUserWithTodayBirthday() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("valid_login");
        user.setBirthday(LocalDate.now());

        assertDoesNotThrow(() -> userController.create(user));
    }

    @Test
    void shouldSetNameToLoginIfNameIsBlank() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("my_login");
        user.setName(""); // пустое имя
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User created = userController.create(user);
        assertEquals("my_login", created.getName());
    }

    @Test
    void shouldSetNameToLoginIfNameIsNull() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("my_login");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User created = userController.create(user);
        assertEquals("my_login", created.getName());
    }
}