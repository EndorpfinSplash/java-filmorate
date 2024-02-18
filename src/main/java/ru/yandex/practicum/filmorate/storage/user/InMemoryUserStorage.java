package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private int idCounter = 1;
    private final HashMap<Integer, User> users = new HashMap<>();


    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user.getId() == null) {
            user.setId(idCounter);
        }
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException(user + " already exists in the list");
        }
        users.put(user.getId(), user);
        idCounter++;
        return user;
    }

    @Override
    public User update(User user) {
        Integer userId = user.getId();
        if (users.containsKey(userId)) {
            users.put(userId, user);

            return user;
        }
        throw new UserNotFoundException(String.format("User with id=%s absent", userId));
    }

    @Override
    public User getUser(Integer id) {
        return users.computeIfAbsent(id, integer -> {
            throw new UserNotFoundException(String.format("User with id=%s absent", id));
        });
    }
}
