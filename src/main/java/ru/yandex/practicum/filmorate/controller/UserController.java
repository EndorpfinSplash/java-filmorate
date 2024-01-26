package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAll() {
        return new ArrayList(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        String email = user.getEmail();

        if (email == null || email.isBlank()) {
            throw new ValidationException("Invalid email");
        }

        if (users.containsKey(email)) {
            throw new UserAlreadyExistException("User already exists");
        }

        users.put(user.getId(), user);
        log.info("User was created");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {

        String email = user.getEmail();

        if (email == null || email.isBlank()) {
            throw new ValidationException("Invalid email");
        }

        users.put(user.getId(), user);
        log.info("User was updated");
        return user;
    }

}
