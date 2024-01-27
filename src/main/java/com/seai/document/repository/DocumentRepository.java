package com.seai.document.repository;

import com.seai.document.model.MarineDocument;
import com.seai.exception.NotFoundException;
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
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DocumentRepository {

    private static final String FIND_DOCUMENTS_BY_USER_ID_QUERY = "SELECT id, user_id, name, number, issued_date, expiry_date, is_verified, created_date, path FROM documents WHERE user_id= ?";

    private static final String FIND_VERIFIED_DOCUMENTS_BY_MULTIPLE_IDS = "SELECT id, name, number, issued_date, expiry_date, is_verified, created_date, path FROM documents WHERE ID IN (:ids) and is_verified = true";

    private static final String FIND_VERIFIED_DOCUMENT_BY_USER_ID_AND_DOCUMENT_ID_QUERY = "SELECT id, name, number, issued_date, expiry_date, is_verified, created_date, path FROM documents WHERE user_id= ? and id= ? and is_verified = true";
    private static final String FIND_DOCUMENT_BY_USER_ID_AND_DOCUMENT_ID_QUERY = "SELECT id, user_id, name, number, issued_date, expiry_date, is_verified, created_date, path FROM documents WHERE user_id= ? and id= ?";

    private static final String SAVE_DOCUMENT_QUERY = "INSERT INTO documents (id, name, number, issued_date, expiry_date, is_verified, created_date, user_id, path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String VERIFY_DOCUMENT_QUERY = "UPDATE documents SET name = ?, number = ?, issued_date = ?, expiry_date = ?, is_verified = ? where id = ? and user_id = ?";

    private static final String DELETE_DOCUMENT_QUERY = "DELETE FROM documents WHERE id = ? and user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MarineDocument find(UUID userId, UUID documentId) {
        try {
            return jdbcTemplate.queryForObject(FIND_DOCUMENT_BY_USER_ID_AND_DOCUMENT_ID_QUERY,
                    getMarineDocumentRowMapper(),
                    userId.toString(),
                    documentId.toString());
        } catch (Exception e) {
            throw new NotFoundException(String.format("Document with id %s not found", documentId.toString()));
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

    public void save(MarineDocument marineDocument, UUID userId) {
        marineDocument.setPath(String.format("%s/%s/%s", userId, marineDocument.getName(), marineDocument.getId()));
        jdbcTemplate.update(SAVE_DOCUMENT_QUERY,
                marineDocument.getId(),
                marineDocument.getName(),
                marineDocument.getNumber(),
                marineDocument.getIssueDate(),
                marineDocument.getExpiryDate(),
                marineDocument.isVerified(),
                Timestamp.from(Instant.now()),
                userId.toString(),
                marineDocument.getPath());
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
                new Date(rs.getObject("expiry_date", java.sql.Date.class).getTime()),
                rs.getObject("is_verified", Boolean.class),
                rs.getTimestamp("created_date").toInstant(),
                rs.getString("path"));
    }

    public List<MarineDocument> findByIds(List<UUID> ids) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(FIND_VERIFIED_DOCUMENTS_BY_MULTIPLE_IDS, parameters, getMarineDocumentRowMapper());
    }

    public MarineDocument findVerifiedDocument(UUID userId, UUID documentId) {
        return jdbcTemplate.queryForObject(FIND_VERIFIED_DOCUMENT_BY_USER_ID_AND_DOCUMENT_ID_QUERY,
                getMarineDocumentRowMapper(),
                userId.toString(),
                documentId.toString());
    }
}
