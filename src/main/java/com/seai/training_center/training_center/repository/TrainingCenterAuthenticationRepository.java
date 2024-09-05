package com.seai.training_center.training_center.repository;

import com.seai.common.exception.ResourceNotFoundException;
import com.seai.marine.user.model.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TrainingCenterAuthenticationRepository {

    private static final String REGISTER_TRAINING_CENTER_QUERY = "INSERT INTO training_centers_auth (id, email, password)" +
            " VALUES (?, ?, ?)";

    private static final String FIND_TRAINING_CENTER_BY_EMAIL_QUERY = "SELECT id, email, password FROM training_centers_auth WHERE email= ?";

    private final PasswordEncoder encoder;

    private final JdbcTemplate jdbcTemplate;

    public void save(UserAuthentication userAuthentication, UUID id) {
        jdbcTemplate.update(REGISTER_TRAINING_CENTER_QUERY, id,
                userAuthentication.getEmail(),
                encoder.encode(userAuthentication.getPassword()));
    }

    public UserAuthentication findByEmail(String email) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_TRAINING_CENTER_BY_EMAIL_QUERY,
                    (rs, rowNum) -> new UserAuthentication(UUID.fromString(rs.getString("id")),
                            rs.getString("email"),
                            rs.getString("password")), email);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("User with email not found : " + email);
        }
    }
}
