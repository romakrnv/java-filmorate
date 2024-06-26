package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

@lombok.Data
public class User {
    Long id;
    @Email
    @NotNull
    @NotBlank
    String email;
    @NotNull
    String login;
    String name;
    @Past
    LocalDate birthday;
}
