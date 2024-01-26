package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
@EqualsAndHashCode
public class User {
    private Integer id;
    @Email
    @NotNull
    private String email;
    @NotBlank(message = "login may not be null")
    private String login;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Past
    private LocalDate birthday;

    public String getName() {
        return (name == null || name.isEmpty()) ? login : name;
    }

    //    @EqualsAndHashCode.Exclude
}
