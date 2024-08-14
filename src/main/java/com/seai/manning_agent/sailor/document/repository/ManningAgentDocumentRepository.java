package com.seai.manning_agent.sailor.document.repository;

import com.seai.exception.ResourceNotFoundException;
import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.model.MarineDocumentWithEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ManningAgentDocumentRepository {

    private static final String FIND_DOCUMENTS_BY_USER_ID_QUERY = "SELECT id, user_id, name, number, issued_date, expiry_date, is_verified, created_date, path FROM documents WHERE user_id= ?";

    private static final String FIND_DOCUMENTS_FOR_USERS_WITH_REMINDERS = """
    SELECT d.id, d.user_id, d.name, d.number, d.issued_date, d.expiry_date, d.is_verified, d.created_date, d.path, r.email
    FROM documents d
    JOIN reminders r ON d.user_id = r.user_id
    WHERE d.expiry_date IN (
        SELECT date_series
        FROM generate_series(
            current_date,
            current_date + interval '1 year',
            interval '1 month'
        ) AS date_series
        WHERE
            (extract(day from current_date) <= extract(day from date_series)
            AND extract(day from current_date) = extract(day from date_series))
            OR (extract(day from current_date) > extract(day from date_series)
            AND date_series = (date_trunc('month', date_series) + interval '1 month' - interval '1 day')::date)
    )
""";

    private static final String FIND_DOCUMENT_BY_USER_ID_AND_DOCUMENT_ID_QUERY = "SELECT id, user_id, name, number, issued_date, expiry_date, is_verified, created_date, path FROM documents WHERE user_id= ? and id= ?";

    private static final String SAVE_DOCUMENT_QUERY = "INSERT INTO documents (id, name, number, issued_date, expiry_date, is_verified, created_date, user_id, path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String VERIFY_DOCUMENT_QUERY = "UPDATE documents SET name = ?, number = ?, issued_date = ?, expiry_date = ?, is_verified = ? where id = ? and user_id = ?";

    private static final String DELETE_DOCUMENT_QUERY = "DELETE FROM documents WHERE id = ? and user_id = ?";

    private static final String DELETE_ALL_DOCUMENT_QUERY = "DELETE FROM documents WHERE user_id = ?";

    private final JdbcTemplate jdbcTemplate;


    public MarineDocument find(UUID userId, UUID documentId) {
        try {
            return jdbcTemplate.queryForObject(FIND_DOCUMENT_BY_USER_ID_AND_DOCUMENT_ID_QUERY,
                    getMarineDocumentRowMapper(),
                    userId.toString(),
                    documentId.toString());
        } catch (Exception e) {
            throw new ResourceNotFoundException(String.format("Document with id %s not found", documentId.toString()));
        }
    }

    public List<MarineDocument> findAll(UUID userId) {
        return jdbcTemplate.query(FIND_DOCUMENTS_BY_USER_ID_QUERY,
                getMarineDocumentRowMapper(),
                userId.toString());
    }

    public void delete(UUID documentId, UUID userId) {
        jdbcTemplate.update(DELETE_DOCUMENT_QUERY, documentId.toString(), userId.toString());
    }

    public void deleteAll(UUID userId) {
        jdbcTemplate.update(DELETE_ALL_DOCUMENT_QUERY, userId.toString());
    }

    public MarineDocument save(MarineDocument marineDocument, UUID sailorId) {
        marineDocument.setPath(String.format("%s/%s/%s", sailorId, marineDocument.getName(), marineDocument.getId()));
        jdbcTemplate.update(SAVE_DOCUMENT_QUERY,
                marineDocument.getId(),
                marineDocument.getName(),
                marineDocument.getNumber(),
                marineDocument.getIssueDate(),
                marineDocument.getExpiryDate(),
                marineDocument.isVerified(),
                Timestamp.from(Instant.now()),
                sailorId.toString(),
                marineDocument.getPath());
        return marineDocument;
    }

    public void update(MarineDocument marineDocument, UUID userId, UUID id) {
        jdbcTemplate.update(VERIFY_DOCUMENT_QUERY,
                marineDocument.getName(),
                marineDocument.getNumber(),
                marineDocument.getIssueDate(),
                marineDocument.getExpiryDate(),
                marineDocument.isVerified(),
                id.toString(),
                userId.toString());
    }

    private static RowMapper<MarineDocument> getMarineDocumentRowMapper() {
        return (rs, rowNum) -> new MarineDocument(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("user_id")),
                rs.getString("name"),
                rs.getString("number"),
                new Date(rs.getObject("issued_date", java.sql.Date.class).getTime()),
                Optional.ofNullable(rs.getObject("expiry_date", java.sql.Date.class))
                        .map(d -> new Date(d.getTime())).orElse(null),
                rs.getObject("is_verified", Boolean.class),
                rs.getTimestamp("created_date").toInstant(),
                rs.getString("path"));
    }
}