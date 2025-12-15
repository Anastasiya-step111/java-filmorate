package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @GetMapping
    public List<User> findAll() {
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validateUser(user);
        user.setId(idGenerator.getAndIncrement());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.add(user);
        log.info("Добавлен новый пользователь: {}", user.getLogin());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        validateUserForUpdate(user);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                // Обновляем имя, если оно пустое — ставим логин
                if (user.getName() == null || user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                users.set(i, user);
                log.info("Пользователь обновлён: {}", user.getLogin());
                return user;
            }
        }
        throw new ValidationException("Пользователь с id=" + user.getId() + " не найден");
    }

    private void validateUser(User user) {
        validateEmail(user.getEmail());
        validateLogin(user.getLogin());
        validateBirthday(user.getBirthday());
    }

    private void validateUserForUpdate(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id пользователя должен быть указан");
        }
        validateEmail(user.getEmail());
        validateLogin(user.getLogin());
        validateBirthday(user.getBirthday());
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать '@'");
        }
    }

    private void validateLogin(String login) {
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
    }

    private void validateBirthday(LocalDate birthday) {
        if (birthday != null && birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
