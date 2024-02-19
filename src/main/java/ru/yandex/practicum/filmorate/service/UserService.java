package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
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
        return userStorage.getUserById(id);
    }

    public void createFriendship(Integer friend1, Integer friend2) {
        User friend1User = userStorage.getUserById(friend1);
        User friend2User = userStorage.getUserById(friend2);
        friend1User.getFriends().add(friend2);
        friend2User.getFriends().add(friend1);
    }

    public void deleteFriendship(Integer friend1, Integer friend2) {
        User friend1User = userStorage.getUserById(friend1);
        User friend2User = userStorage.getUserById(friend2);
        friend1User.getFriends().remove(friend2);
        friend2User.getFriends().remove(friend1);
    }

    public List<User> getAllUserFriends(Integer userId) {
        User user = userStorage.getUserById(userId);
        return user.getFriends().stream().map(userStorage::getUserById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        User user = getUserById(id);
        User otherUser = getUserById(otherId);

        Set<Integer> intersectedFriendsId = new HashSet<>(user.getFriends());
        intersectedFriendsId.retainAll(otherUser.getFriends());

        return intersectedFriendsId.stream().map(userStorage::getUserById).collect(Collectors.toList());
    }
}
