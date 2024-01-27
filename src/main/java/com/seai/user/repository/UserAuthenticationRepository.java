package com.seai.user.repository;

import com.seai.exception.NotFoundException;
import com.seai.user.model.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserAuthenticationRepository {

    private static final String REGISTER_USER_QUERY = "INSERT INTO users (id, email, password)" +
            " VALUES (?, ?, ?)";

    private static final String FIND_USER_BY_EMAIL_QUERY = "SELECT id, email, password FROM users WHERE email= ?";

    private final PasswordEncoder encoder;

    private final JdbcTemplate jdbcTemplate;

    public void save(UserAuthentication userAuthentication, UUID id) {
        jdbcTemplate.update(REGISTER_USER_QUERY, id,
                userAuthentication.getEmail(),
                encoder.encode(userAuthentication.getPassword()));
    }

    public UserAuthentication findByEmail(String email) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL_QUERY,
                    (rs, rowNum) -> new UserAuthentication(UUID.fromString(rs.getString("id")),
                            rs.getString("email"),
                            rs.getString("password")), email);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("User with email not found : " + email);
        }
    }
}
