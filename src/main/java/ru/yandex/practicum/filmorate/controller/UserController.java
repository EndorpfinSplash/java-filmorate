package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("GET request to fetch collection of users received.");
        return userService.getAll();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("POST request to create " + user + " received.");
        validateUser(user);
        User createdUser = userService.create(user);
        log.info(user + " was created");
        return createdUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT request to update " + user + "  received.");
        validateUser(user);
        User createdUser = userService.update(user);
        log.info(user + " was updated");
        return createdUser;
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Spaces in login forbidden!");
        }
    }

}
