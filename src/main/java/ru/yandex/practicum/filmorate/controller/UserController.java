package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAbsentException;
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
    private int idCounter = 1;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        log.info("GET request to fetch list of users received.");
        return new ArrayList(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("POST request to create user received.");
        user.setId(idCounter);

        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException(user +" already exists in the list");
        }
        validateUser(user);
        users.put(user.getId(), user);
        idCounter++;
        log.info(user + " was created");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT request to update user received.");
        Integer userId = user.getId();
        if (users.containsKey(userId)) {
            validateUser(user);
            users.put(userId, user);
            log.info(user +" was updated");
            return user;
        }
        log.info(user +"is absent");
        throw new UserAbsentException(user +" is absent");
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Spaces in login forbidden!");
        }
    }

}
