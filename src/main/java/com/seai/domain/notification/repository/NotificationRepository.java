package com.seai.domain.notification.repository;

import com.seai.DatabaseUtil;
import com.seai.domain.notification.model.DocumentNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<DocumentNotification> getAllNotifications() {
        String sql = "SELECT * FROM notifications";
        return jdbcTemplate.query(sql, new NotificationRowMapper());
    }

    public List<DocumentNotification> getCurrentNotifications(Instant notifyAt, Date notifyDay) {
        String sql = "SELECT * FROM notifications WHERE notify_at = ? AND notify_day = ?";
        return jdbcTemplate.query(sql, new NotificationRowMapper(), notifyAt, notifyDay);
    }

    public DocumentNotification getNotificationById(String id) {
        String sql = "SELECT * FROM notifications WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new NotificationRowMapper(), id);
    }

    public void saveNotification(DocumentNotification notification) {
        String sql = "INSERT INTO notifications (id, document_id, notify_at, notify_day) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                notification.getId(),
                notification.getDocumentId(),
                notification.getNotifyAt(),
                notification.getNotifyDay()
        );
    }

    public void deleteNotification(String id) {
        String sql = "DELETE FROM notifications WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class NotificationRowMapper implements RowMapper<DocumentNotification> {
        @Override
        public DocumentNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
            DocumentNotification notification = new DocumentNotification();
            notification.setId(DatabaseUtil.getUuid(rs, "id"));
            notification.setDocumentId(DatabaseUtil.getUuid(rs, "document_id"));
            notification.setNotifyAt(DatabaseUtil.getInstant(rs, "notify_at"));
            notification.setNotifyDay(DatabaseUtil.getDate(rs, "notify_day"));
            return notification;
        }
    }
}
