package com.seai.spring.security.password_reset.repository;

import com.seai.spring.security.password_reset.model.PasswordResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PasswordResetRepository {

    private static final String SAVE_TOKEN_QUERY = "INSERT INTO password_reset_tokens (id, token, created_at, expired_at, user_id) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_TOKEN_QUERY = "SELECT * FROM password_reset_tokens WHERE token=?";

    private static final String DELETE_TOKEN_QUERY = "DELETE FROM password_reset_tokens WHERE user_id=?";

    private final JdbcTemplate jdbcTemplate;


    public PasswordResetToken save(PasswordResetToken token) {
        jdbcTemplate.update(SAVE_TOKEN_QUERY,
                token.getId(),
                token.getToken(),
                token.getCreatedAt(),
                token.getExpiredAt(),
                token.getUserId());
        return token;
    }


    public PasswordResetToken findByToken(String token) {
        try {
            return jdbcTemplate.queryForObject(FIND_TOKEN_QUERY, tokenRowMapper(), token);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void delete(UUID id) {
        jdbcTemplate.update(DELETE_TOKEN_QUERY, id.toString());
    }


    private RowMapper<PasswordResetToken> tokenRowMapper() {
        return (rs, rowNum) -> new PasswordResetToken(
                UUID.fromString(rs.getString("id")),
                rs.getString("token"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("expired_at").toLocalDateTime(),
                UUID.fromString(rs.getString("user_id"))
        );
    }
}
