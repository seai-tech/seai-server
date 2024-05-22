package com.seai.marine.notification.repository;

import com.seai.marine.notification.model.Reminder;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReminderRepository {

    private static final String CREATE_REMINDER_QUERY = "INSERT INTO reminders (id, user_id, email) VALUES (?, ?, ?)";
    private static final String DELETE_REMINDER_QUERY = "DELETE FROM reminders WHERE user_id = ?";
    private static final String FIND_REMINDER_BY_USER_ID_QUERY = "SELECT id, user_id, email FROM reminders WHERE user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public void saveReminder(UUID user_id, String user_email) {
        jdbcTemplate.update(CREATE_REMINDER_QUERY, UUID.randomUUID(), user_id, user_email);
    }

    public void deleteReminder(UUID userId) {
        jdbcTemplate.update(DELETE_REMINDER_QUERY, userId.toString());
    }

    public Optional<Reminder> findReminderByUserId(UUID userId) {
        List<Reminder> reminders = jdbcTemplate.query(FIND_REMINDER_BY_USER_ID_QUERY, getReminderRowMapper(), userId.toString());
        return reminders.stream().findFirst();
    }

    private static RowMapper<Reminder> getReminderRowMapper() {
        return (rs, rowNum) -> new Reminder(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("user_id")),
                rs.getString("email")
        );
    }
}
