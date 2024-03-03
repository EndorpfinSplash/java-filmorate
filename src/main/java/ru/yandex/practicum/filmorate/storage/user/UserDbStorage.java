package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "select * from APPLICATION_USER";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs) );
    }
    private User makeUser( ResultSet rs) throws SQLException {

        User user = User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();

        Set<Integer> userFriends = user.getFriends();
//        Set<Integer> userFriends = user.getFriends();
        return user;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from APPLICATION_USER where id = ?", id);
        if (userRows.next()) {
            User user = User.builder()
                    .id(userRows.getInt("id"))
                    .name(userRows.getString("name"))
                    .login(userRows.getString("login"))
                    .email(userRows.getString("email"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .build();

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }

    }
}
