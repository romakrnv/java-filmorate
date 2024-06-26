package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@lombok.Data
public class Film {
    Long id;
    @NotNull
    @NotBlank
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;
}
