package com.seai.marine.user.repository;

import com.seai.exception.DuplicatedResourceException;
import com.seai.exception.ResourceNotFoundException;
import com.seai.marine.user.model.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserAuthenticationRepository {

    private static final String REGISTER_USER_QUERY = "INSERT INTO users_auth (id, email, password)" +
            " VALUES (?, ?, ?)";

    private static final String FIND_ALL_USERS_QUERY = "SELECT id, email, password FROM users_auth";

    private static final String FIND_USER_BY_EMAIL_QUERY = "SELECT id, email, password FROM users_auth WHERE email= ?";

    private static final String FIND_USER_BY_USERNAME_QUERY = "SELECT id, email, username, password FROM users_auth WHERE username= ?";

    private static final String DELETE_USER = "DELETE FROM users_auth WHERE id= ?";

    private final PasswordEncoder encoder;

    private final JdbcTemplate jdbcTemplate;

    public void save(UserAuthentication userAuthentication, UUID id) {
        try {
            jdbcTemplate.update(REGISTER_USER_QUERY, id,
                    userAuthentication.getEmail(),
                    encoder.encode(userAuthentication.getPassword()));
        } catch (DuplicateKeyException e) {
            throw new DuplicatedResourceException("User with email %s already exists", userAuthentication.getEmail());
        }
    }

    public List<UserAuthentication> findAllUsers() {
        return jdbcTemplate.query(FIND_ALL_USERS_QUERY, (rs, rowNum) -> new UserAuthentication(
                UUID.fromString(rs.getString("id")),
                rs.getString("email"),
                rs.getString("password")));
    }

    public UserAuthentication findByEmail(String email) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL_QUERY,
                    (rs, rowNum) -> new UserAuthentication(UUID.fromString(rs.getString("id")),
                            rs.getString("email"),
                            rs.getString("password")), email);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Invalid email or password : " + email);
        }
    }

    public UserAuthentication findByUsername(String username) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_USERNAME_QUERY,
                    (rs, rowNum) -> new UserAuthentication(
                            UUID.fromString(rs.getString("id")),
                            rs.getString("email"),
                            rs.getString("password")), username);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Invalid username : " + username);
        }
    }

    public void delete(UUID uuid) {
        jdbcTemplate.update(DELETE_USER, uuid.toString());
    }
}
