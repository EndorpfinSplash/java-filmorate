package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    Collection<User> getAllUsers();

    User saveUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> getUserById(Integer id);

    boolean initFriendship(final User initiator, final User approver);

    boolean deleteFriendship(final User initiator, final User approver);

    Set<Integer> getUserFriendsId(Integer user2Id);
}
