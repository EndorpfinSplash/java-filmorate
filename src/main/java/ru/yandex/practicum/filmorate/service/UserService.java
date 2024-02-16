package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
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

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getUser(Integer id) {
        return userStorage.getUser(id);
    }

    public void createFriendship(Integer friend1, Integer friend2) {
        User friend1User = getUser(friend1);
        User friend2User = getUser(friend2);
        friend1User.getFriends().add(friend2);
        friend2User.getFriends().add(friend1);
    }

    public void deleteFriendship(Integer friend1, Integer friend2) {
        User friend1User = getUser(friend1);
        User friend2User = getUser(friend2);
        friend1User.getFriends().remove(friend2);
        friend2User.getFriends().remove(friend1);
    }

    public List<User> getFriends(Integer userId) {
        User user = getUser(userId);
        return user.getFriends().stream().map(userStorage::getUser).collect(Collectors.toList());
    }


    public List<User> getCommonFriends(Integer id, Integer otherId) {
        User user = getUser(id);
        User otherUser = getUser(otherId);

        Set<Integer> intersectedFriendsId = user.getFriends();
        intersectedFriendsId.retainAll(otherUser.getFriends());

        return intersectedFriendsId.stream().map(userStorage::getUser).collect(Collectors.toList());
    }
}
