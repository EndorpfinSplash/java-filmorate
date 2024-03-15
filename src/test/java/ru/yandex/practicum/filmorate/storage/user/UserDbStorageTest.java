package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@JdbcTest
//@SpringBootTest
//@Sql({"schema.sql", "data.sql"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private UserDbStorage userDbStorage;

    @BeforeEach
    void initPreparation() {
        userDbStorage = new UserDbStorage(this.jdbcTemplate);
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "APPLICATION_USER", "FRIENDSHIP");
    }

    @Test
    void testGetUserById() {
        User newUser = User.builder()
                .login("vanya123")
                .name("Ivan Petrov")
                .email("user@email.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Integer savedId = userDbStorage.saveUser(newUser).getId();

        User savedUser = userDbStorage.getUserById(savedId).get();

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    void test_getAllUsers() {
        User testUser1 = User.builder()
                .login("testUser1")
                .name("John Petrov")
                .email("user1@email.us")
                .birthday(LocalDate.of(1970, 1, 1))
                .build();
        userDbStorage.saveUser(testUser1);

        User testUser2 = User.builder()
                .login("testUser2")
                .name("John Testovich")
                .email("userTest@email.us")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        userDbStorage.saveUser(testUser2);

        Collection<User> allUsers = userDbStorage.getAllUsers();
        Arrays.asList(testUser1, testUser2).forEach(
                user -> assertTrue(allUsers.contains(user), user + " absent in User's list")
        );
        assertEquals(2, allUsers.size(), "incorrect quantity of user");
    }

    @Test
    void test_saverUser() {
        User newUser = User.builder()
                .login("john")
                .name("Ivan Smith")
                .email("user_john@email.us")
                .birthday(LocalDate.of(1999, 1, 1))
                .build();

        User savedUser = userDbStorage.saveUser(newUser);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    void updateUser() {
        User newUser = User.builder()
                .login("vanya123")
                .name("Ivan Petrov")
                .email("user@email.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Integer savedUserId = userDbStorage.saveUser(newUser).getId();

        User userUpdated = User.builder()
                .id(savedUserId)
                .login("updatedLogin")
                .name("Van Petrovich")
                .email("userOK@email.us")
                .birthday(LocalDate.of(1995, 5, 1))
                .build();

        User userUpdatedAndSaved = userDbStorage.updateUser(userUpdated).get();

        assertThat(userUpdated)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userUpdatedAndSaved);
    }

    @Test
    void test_getUserFriendsId() {
        User testUser1 = User.builder()
                .login("testUser1")
                .name("John Petrov")
                .email("user1@email.us")
                .birthday(LocalDate.of(1970, 1, 1))
                .build();
        Integer user1Id = userDbStorage.saveUser(testUser1).getId();

        User testUser2 = User.builder()
                .login("testUser2")
                .name("John Testovich")
                .email("userTest@email.us")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        Integer user2Id = userDbStorage.saveUser(testUser2).getId();

        User testUser3 = User.builder()
                .login("testUser3")
                .name("test Testovich")
                .email("Test@email.us")
                .birthday(LocalDate.of(1999, 1, 1))
                .build();
        Integer user3Id = userDbStorage.saveUser(testUser3).getId();

        userDbStorage.initFriendship(testUser1, testUser2);
        userDbStorage.initFriendship(testUser3, testUser2);

        Set<Integer> user2FriendsId = userDbStorage.getUserFriendsId(user2Id);
        int actualUser2FriendListSize = user2FriendsId.size();
        assertEquals(2, actualUser2FriendListSize, "TestUser2 has to have 2 friends");

        Arrays.asList(user1Id, user3Id).forEach(
                userId -> assertTrue(user2FriendsId.contains(userId))
        );


        Set<Integer> user1FriendsId = userDbStorage.getUserFriendsId(user1Id);
        int actualUser1FriendListSize = user1FriendsId.size();
        assertEquals(0, actualUser1FriendListSize, "TestUser1 has to have 0 friends");

        Set<Integer> user3FriendsId = userDbStorage.getUserFriendsId(user3Id);
        int actualUser3FriendListSize = user3FriendsId.size();
        assertEquals(0, actualUser3FriendListSize, "TestUser3 has to have 0 friends");

    }

    @Test
    void initFriendship() {
    }

    @Test
    void deleteFriendship() {
    }
}