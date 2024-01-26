package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectAbsentException;
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
    static int idCounter = 1;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAll() {
        return new ArrayList(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        user.setId(idCounter);

        if (users.containsKey(user.getId())) {
            throw new ObjectAbsentException("User already exists");
        }
        validate(user);
        users.put(user.getId(), user);
        idCounter++;
        log.info("User was created");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        Integer userId = user.getId();
        if (users.containsKey(userId)) {
            validate(user);
            users.put(userId, user);
            log.info("User was updated");
            return user;
        }
        log.info("This user absent");
        throw new ObjectAbsentException("This user absent");
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Spaces in login forbidden!");
        }
    }

}
