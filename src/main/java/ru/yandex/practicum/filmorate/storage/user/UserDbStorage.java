package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


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
        String sql = "select * from APPLICATION_USER order by ID";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    int userId = rs.getInt("id");
                    User user = User.builder()
                            .id(userId)
                            .name(rs.getString("name"))
                            .login(rs.getString("login"))
                            .email(rs.getString("email"))
                            .birthday(rs.getDate("birthday").toLocalDate())
                            .build();

                    user.getFriends().addAll(getUserFriendsId(userId));

                    return user;
                });
    }

    @Override
    public User saveUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("APPLICATION_USER")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", user.getId());
        parameters.put("NAME", user.getName());
        parameters.put("LOGIN", user.getLogin());
        parameters.put("EMAIL", user.getEmail());
        parameters.put("BIRTHDAY", user.getBirthday());

        Integer id = (Integer) simpleJdbcInsert.executeAndReturnKey(parameters);
        user.setId(id);
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        int updated = jdbcTemplate.update(
                "UPDATE APPLICATION_USER set Name =?, LOGIN =?, EMAIL=?, BIRTHDAY = ? where ID = ?",
                user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return updated == 1 ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from APPLICATION_USER where id = ?", userId);
        if (userRows.next()) {
            User user = User.builder()
                    .id(userRows.getInt("id"))
                    .name(userRows.getString("name"))
                    .login(userRows.getString("login"))
                    .email(userRows.getString("email"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .build();
            user.getFriends().addAll(getUserFriendsId(userId));

//            log.info("Найден пользователь: id={} name={}", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            return Optional.empty();
        }

    }

    public Set<Integer> getUserFriendsId(Integer userId) {
        String sql =
                "select APPROVER from FRIENDSHIP where INITIATOR = ? and APPROVE_DATE is not null " +
                        "union " +
                        "select INITIATOR from FRIENDSHIP where APPROVER = ? ";
        List<Integer> friendsIds = jdbcTemplate.queryForList(sql, Integer.class, userId, userId);
        return new HashSet<>(friendsIds);
    }

    public boolean initFriendship(final User initiator, final User approver) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FRIENDSHIP")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("INITIATOR", initiator.getId());
        parameters.put("APPROVER", approver.getId());
//        parameters.put("APPROVE_DATE", LocalDate.now());
        int execute = simpleJdbcInsert.execute(parameters);
        return execute == 1;
    }

    public boolean deleteFriendship(final User initiator, final User approver) {
        String sql = "DELETE FROM FRIENDSHIP WHERE INITIATOR = ? and APPROVER = ? ";
        Object[] args = new Object[]{initiator.getId(), approver.getId()};
        return jdbcTemplate.update(sql, args) == 1;
    }
}
