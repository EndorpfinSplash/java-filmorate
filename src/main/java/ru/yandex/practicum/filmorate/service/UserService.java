package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id)
                .orElseThrow(() ->new UserNotFoundException(MessageFormat.format("User with id={} not found", id)));
    }

    public void createFriendship(Integer friend1, Integer friend2) {
        User friend1User = userStorage.getUserById(friend1)
                .orElseThrow(() ->new UserNotFoundException(MessageFormat.format("User with id={} not found", friend1)));
        User friend2User = userStorage.getUserById(friend2)
                .orElseThrow(() ->new UserNotFoundException(MessageFormat.format("User with id={} not found", friend2)));
        friend1User.getFriends().add(friend2);
        friend2User.getFriends().add(friend1);
    }

    public void deleteFriendship(Integer friend1, Integer friend2) {
        User friend1User = userStorage.getUserById(friend1)
                .orElseThrow(() ->new UserNotFoundException(MessageFormat.format("User with id={} not found", friend1)));
        User friend2User = userStorage.getUserById(friend2)
                .orElseThrow(() ->new UserNotFoundException(MessageFormat.format("User with id={} not found", friend2)));

        friend1User.getFriends().remove(friend2);
        friend2User.getFriends().remove(friend1);
    }

    public List<User> getAllUserFriends(Integer userId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() ->new UserNotFoundException(MessageFormat.format("User with id={} not found", userId)));
        return user.getFriends().stream()
                .map(userIdentifier -> userStorage.getUserById(userIdentifier).get())
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        User user = userStorage.getUserById(id)
                .orElseThrow(() ->new UserNotFoundException(MessageFormat.format("User with id={} not found", id)));

        User otherUser = userStorage.getUserById(otherId)
                .orElseThrow(() ->new UserNotFoundException(MessageFormat.format("User with id={} not found", otherId)));

        Set<Integer> intersectedFriendsId = new HashSet<>(user.getFriends());
        intersectedFriendsId.retainAll(otherUser.getFriends());

        return intersectedFriendsId.stream()
                .map(userId -> userStorage.getUserById(userId).get())
                .collect(Collectors.toList());
    }
}
