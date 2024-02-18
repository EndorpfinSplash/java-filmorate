package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    private Set<Integer> friends;


    public String getName() {
        return (name == null || name.isEmpty()) ? login : name;
    }

    public Set<Integer> getFriends() {
        if (friends == null) {
            friends = new HashSet<>();
        }
        System.out.println("User login ={" + this.login + "} this.friends = " + this.friends);
        return this.friends;
    }

    //    @EqualsAndHashCode.Exclude
}
