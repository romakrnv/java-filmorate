package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import static ru.yandex.practicum.filmorate.storage.queries.UserSqlQueries.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Primary
@Component
public class DBUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAllUsers() {
        return jdbcTemplate.query(FIND_ALL_USERS_QUERY, new UserRowMapper());
    }

    @Override
    public User getUser(Long id) {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_ID_QUERY, new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User with id: " + id + " not found");
        }
    }

    @Override
    public User addUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(ADD_USER_QUERY, new String[]{"ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User newUser) {
        int rowsAffected = jdbcTemplate.update(UPDATE_USER_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                Date.valueOf(newUser.getBirthday()),
                newUser.getId());
        if (rowsAffected == 0) {
            throw new NotFoundException("user with id: " + newUser.getId() + " not found");
        }
        return newUser;
    }

    @Override
    public void addFriend(Long id1, Long id2) {
        jdbcTemplate.update(ADD_FRIEND_QUERY, id1, id2);
    }

    @Override
    public void removeFriend(Long id1, Long id2) {
        jdbcTemplate.update(REMOVE_FRIEND_QUERY, id1, id2);
    }
}
