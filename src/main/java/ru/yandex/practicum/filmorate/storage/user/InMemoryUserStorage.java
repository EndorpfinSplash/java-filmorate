package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private int idCounter = 1;
    private final Map<Integer, User> users = new HashMap<>();


    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        user.setId(idCounter);
        users.put(user.getId(), user);
        idCounter++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        Integer userId = user.getId();
        if (users.containsKey(userId)) {
            users.put(userId, user);
            return user;
        }
        throw new UserNotFoundException(String.format("User with id=%d absent", userId));
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.of(users.get(id));
    }
}
