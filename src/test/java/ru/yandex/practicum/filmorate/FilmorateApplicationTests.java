package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "spring.main.banner-mode=off")
public class FilmorateApplicationTests {

    @Autowired
    private FilmController filmController;

    @Test
    void shouldNotCreateFilmWithBlankName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(100);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldNotCreateFilmWithNullName() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(100);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldNotCreateFilmWithDescriptionLongerThan200Chars() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("a".repeat(201)); // 201 символ
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(100);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldCreateFilmWithDescriptionExactly200Chars() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("a".repeat(200));
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(100);

        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    void shouldNotCreateFilmWithReleaseDateBefore1895_12_28() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27)); // на день раньше
        film.setDuration(100);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldCreateFilmWithReleaseDateExactly1895_12_28() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);

        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    void shouldNotCreateFilmWithZeroDuration() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(0);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldNotCreateFilmWithNegativeDuration() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(-50);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldNotCreateFilmWithNullReleaseDate() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(null);
        film.setDuration(100);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }
}
