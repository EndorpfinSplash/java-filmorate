package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private int idCounter = 1;
    private final Map<Integer, User> users = new HashMap<>();


    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User saveUser(User user) {
        user.setId(idCounter);
        users.put(user.getId(), user);
        idCounter++;
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        Integer userId = user.getId();
        if (users.containsKey(userId)) {
            users.put(userId, user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.of(users.get(id));
    }

    @Override
    public boolean initFriendship(User initiator, User approver) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteFriendship(User initiator, User approver) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> getUserFriendsId(Integer user2Id) {
        throw new UnsupportedOperationException();
    }
}
