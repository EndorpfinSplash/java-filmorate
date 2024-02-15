package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

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

    // как добавление в друзья, удаление из друзей, вывод списка общих друзей.
    public void createFriendship(Integer friend1, Integer friend2) {
        User friend1User = getUser(friend1);
        User friend2User = getUser(friend2);
        friend1User.getFriends().add(friend2);
        friend2User.getFriends().add(friend1);
//        userStorage.update(friend1User);
//        userStorage.update(friend2User);
    }
}
