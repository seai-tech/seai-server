package com.seai.marine.email_verification.repository;

import com.seai.marine.email_verification.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepository {

    private static final String SAVE_TOKEN_QUERY = "INSERT INTO confirmation_tokens (id, token, created_at, expired_at, confirmed_at, user_id) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_TOKEN_QUERY = "UPDATE confirmation_tokens SET token = ?, created_at = ?, expired_at = ?, confirmed_at = ?, user_id = ? WHERE id = ?";

    private static final String FIND_TOKEN_QUERY = "SELECT * FROM confirmation_tokens WHERE token=?";

    private static final String DELETE_EXPIRED_TOKENS_QUERY = "DELETE FROM confirmation_tokens WHERE expired_at < ? AND confirmed_at IS NULL";

    private static final String DELETE_TOKENS_BY_USER_ID_QUERY = "DELETE FROM confirmation_tokens WHERE user_id=?";

    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM confirmation_tokens WHERE user_id = ? AND confirmed_at IS NOT NULL";

    private static final String FIND_VALID_TOKENS_QUERY = "SELECT * FROM confirmation_tokens WHERE expired_at > ? AND confirmed_at IS NULL AND user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public VerificationToken save(VerificationToken token) {
        jdbcTemplate.update(SAVE_TOKEN_QUERY,
                token.getId(),
                token.getToken(),
                token.getCreatedAt(),
                token.getExpiredAt(),
                token.getConfirmedAt() != null ? Timestamp.valueOf(token.getConfirmedAt()) : null,
                token.getUserId());
        return token;
    }

    public VerificationToken update(VerificationToken token) {
            jdbcTemplate.update(UPDATE_TOKEN_QUERY,
                    token.getToken(),
                    token.getCreatedAt(),
                    token.getExpiredAt(),
                    token.getConfirmedAt() != null ? Timestamp.valueOf(token.getConfirmedAt()) : null,
                    token.getUserId(),
                    token.getId().toString());
        return token;
    }

    public Optional<VerificationToken> findByToken(String token) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_TOKEN_QUERY, tokenRowMapper(), token));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteAllExpiredTokens(LocalDateTime now) {
        jdbcTemplate.update(DELETE_EXPIRED_TOKENS_QUERY, Timestamp.valueOf(now));
    }

    public void deleteUserToken(UUID userId) {
        jdbcTemplate.update(DELETE_TOKENS_BY_USER_ID_QUERY, userId.toString());
    }

    public Optional<VerificationToken> findByUserId(UUID userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_USER_ID_QUERY, tokenRowMapper(), userId.toString()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<VerificationToken> findValidToken(UUID userId, LocalDateTime now) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_VALID_TOKENS_QUERY, tokenRowMapper(), Timestamp.valueOf(now), userId.toString()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    private RowMapper<VerificationToken> tokenRowMapper() {
        return (rs, rowNum) -> new VerificationToken(
                UUID.fromString(rs.getString("id")),
                rs.getString("token"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("expired_at").toLocalDateTime(),
                rs.getTimestamp("confirmed_at") != null ? rs.getTimestamp("confirmed_at").toLocalDateTime() : null,
                UUID.fromString(rs.getString("user_id"))
        );
    }
}
