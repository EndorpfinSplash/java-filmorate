package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

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
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        log.info("GET request to fetch user_id={} received.", id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Integer id) {
        log.info("GET request to fetch user_id={} friends received.", id);
        List<User> allUserFriends = userService.getAllUserFriends(id);
        log.info("User with user_id={} has next friends: {}.", id, allUserFriends);
        return allUserFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUserCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        log.info("GET request to fetch user_id={} common friends with other_id={} received.", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("POST request to create {} received.", user);
        validateUser(user);
        User createdUser = userService.createUser(user);
        log.info("{} was created", user);
        return createdUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT request to update {} received.", user);
        validateUser(user);
        User createdUser = userService.updateUser(user);
        log.info("{} was updated", user);
        return createdUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeFriendship(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("PUT request to create friendship with initiator_id ={} and approver_id ={}.", id, friendId);
        userService.createFriendship(friendId, id);
        log.info("Request from initiator_id={} to create friendship with approver_id={} saved.", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendship(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Delete request to remove friendship user_id={} and friend_id={} has created.", id, friendId);
        userService.deleteFriendship(id, friendId);
        log.info("Friendship with user_id={} and friend_id={}  has deleted.", id, friendId);
    }


    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException(String.format("Spaces in login %s forbidden!", user.getLogin()));
        }
    }

}
