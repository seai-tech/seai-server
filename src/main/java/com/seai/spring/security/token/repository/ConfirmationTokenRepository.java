package com.seai.spring.security.token.repository;

import com.seai.spring.security.token.model.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConfirmationTokenRepository {

    private static final String SAVE_TOKEN_QUERY = "INSERT INTO confirmation_tokens (id, token, created_at, expired_at, confirmed_at, user_id) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_TOKEN_QUERY = "UPDATE confirmation_tokens SET token = ?, created_at = ?, expired_at = ?, confirmed_at = ?, user_id = ? WHERE id = ?";

    private static final String FIND_TOKEN_QUERY = "SELECT * FROM confirmation_tokens WHERE token=?";

    private static final String DELETE_TOKEN_QUERY = "DELETE FROM confirmation_tokens WHERE id=?";

    private static final String FIND_EXPIRED_TOKENS_QUERY = "SELECT * FROM confirmation_tokens WHERE expired_at < ? AND confirmed_at IS NULL";

    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM confirmation_tokens WHERE user_id = ? AND confirmed_at IS NOT NULL";

    private static final String FIND_VALID_TOKENS_QUERY = "SELECT * FROM confirmation_tokens WHERE expired_at > ? AND confirmed_at IS NULL AND user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public ConfirmationToken save(ConfirmationToken token) {
        jdbcTemplate.update(SAVE_TOKEN_QUERY,
                token.getId(),
                token.getToken(),
                token.getCreatedAt(),
                token.getExpiredAt(),
                token.getConfirmedAt() != null ? Timestamp.valueOf(token.getConfirmedAt()) : null,
                token.getUserId());
        return token;
    }

    public ConfirmationToken update(ConfirmationToken token) {
            jdbcTemplate.update(UPDATE_TOKEN_QUERY,
                    token.getToken(),
                    token.getCreatedAt(),
                    token.getExpiredAt(),
                    token.getConfirmedAt() != null ? Timestamp.valueOf(token.getConfirmedAt()) : null,
                    token.getUserId(),
                    token.getId().toString());
        return token;
    }

    public ConfirmationToken findByToken(String token) {
        try {
            return jdbcTemplate.queryForObject(FIND_TOKEN_QUERY, tokenRowMapper(), token);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    public void delete(UUID id) {
        jdbcTemplate.update(DELETE_TOKEN_QUERY, id.toString());
    }

    public List<ConfirmationToken> findAllExpiredTokens(LocalDateTime now) {
        return jdbcTemplate.query(FIND_EXPIRED_TOKENS_QUERY, tokenRowMapper(), Timestamp.valueOf(now));
    }

    public Optional<ConfirmationToken> findByUserId(UUID userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_USER_ID_QUERY, tokenRowMapper(), userId.toString()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public ConfirmationToken findValidTokens(UUID userId, LocalDateTime now) {
        try {
            return jdbcTemplate.queryForObject(FIND_VALID_TOKENS_QUERY, tokenRowMapper(), Timestamp.valueOf(now), userId.toString());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    private RowMapper<ConfirmationToken> tokenRowMapper() {
        return (rs, rowNum) -> new ConfirmationToken(
                UUID.fromString(rs.getString("id")),
                rs.getString("token"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("expired_at").toLocalDateTime(),
                rs.getTimestamp("confirmed_at") != null ? rs.getTimestamp("confirmed_at").toLocalDateTime() : null,
                UUID.fromString(rs.getString("user_id"))
        );
    }
}
