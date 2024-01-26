package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode
public class User {
    private int id;
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    private LocalDate birthday;
}
