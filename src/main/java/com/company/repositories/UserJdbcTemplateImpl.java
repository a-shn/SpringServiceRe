package com.company.repositories;

import com.company.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.util.Optional;

public class UserJdbcTemplateImpl implements UserRepository {
    //language=SQL
    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM users_email_password WHERE email = (?)";
    //language=SQL
    private static final String SQL_INSERT = "INSERT INTO users_email_password (login, email, password) VALUES (?,?,?)";

    private JdbcTemplate jdbcTemplate;

    public UserJdbcTemplateImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    private RowMapper<User> userRowMapper = (row, rowNumber) -> {
        String login = row.getString("login");
        String email = row.getString("email");
        String password = row.getString("password");

        return new User(login, password, email);
    };


    @Override
    public void save(User user) {
        int updRows = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            return statement;
        });

        if (updRows == 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL, new Object[]{email}, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
