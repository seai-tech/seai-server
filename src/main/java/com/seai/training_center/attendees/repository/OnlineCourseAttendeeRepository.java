package com.seai.training_center.attendees.repository;

import com.seai.exception.DuplicatedResourceException;
import com.seai.exception.ResourceNotFoundException;
import com.seai.training_center.attendees.model.Attendee;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OnlineCourseAttendeeRepository {

    private static final String SAVE_ATTENDEE_QUERY = "INSERT INTO attendees (id, name, email, telephone, remark, is_waiting, course_id, user_id, online_course_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_ATTENDEE_QUERY = "UPDATE attendees SET name=?, email=?, telephone=?, remark=?, is_waiting=?, course_id=?, user_id=?, online_course_id=? WHERE id=?";

    private static final String FIND_ATTENDEES_QUERY = "SELECT id, name, email, telephone, remark, is_waiting, course_id, user_id, online_course_id FROM attendees WHERE online_course_id=?";

    private static final String FIND_ATTENDEE_BY_ID_QUERY = "SELECT id, name, email, telephone, remark, is_waiting, course_id, user_id, online_course_id FROM attendees WHERE id=?";

    private static final String DELETE_ATTENDEE_QUERY = "DELETE FROM attendees WHERE user_id=? AND online_course_id=?";

    private static final String DELETE_ALL_ATTENDEES_QUERY = "DELETE FROM attendees WHERE online_course_id=?";

    private final JdbcTemplate jdbcTemplate;

    public void save(Attendee attendee) {
        try {
            jdbcTemplate.update(SAVE_ATTENDEE_QUERY,
                    attendee.getId(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendee.getTelephone(),
                    attendee.getRemark(),
                    attendee.getIsWaiting(),
                    attendee.getCourseId(),
                    attendee.getUserId(),
                    attendee.getOnlineCourseId());
        } catch (DuplicateKeyException e) {
            throw new DuplicatedResourceException("Attendee already exists");
        }
    }

    public void update(Attendee attendee) {
        jdbcTemplate.update(UPDATE_ATTENDEE_QUERY,
                attendee.getName(),
                attendee.getEmail(),
                attendee.getTelephone(),
                attendee.getRemark(),
                attendee.getIsWaiting(),
                attendee.getCourseId(),
                attendee.getUserId(),
                attendee.getOnlineCourseId(),
                attendee.getId().toString());
    }

    public List<Attendee> findAll(UUID courseId) {
        return jdbcTemplate.query(FIND_ATTENDEES_QUERY, getAttendeeRowMapper(), courseId.toString());
    }

    public Optional<Attendee> findById(UUID id) throws ResourceNotFoundException {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_ATTENDEE_BY_ID_QUERY,
                getAttendeeRowMapper(), id.toString()));
    }

    public void delete(UUID id, UUID courseId) {
        jdbcTemplate.update(DELETE_ATTENDEE_QUERY,
                id.toString(),
                courseId.toString());
    }

    public void deleteAll(UUID id) {
        jdbcTemplate.update(DELETE_ALL_ATTENDEES_QUERY, id.toString());
    }

    private static RowMapper<Attendee> getAttendeeRowMapper() {
        return (rs, rowNum) -> new Attendee(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("telephone"),
                rs.getString("remark"),
                rs.getBoolean("is_waiting"),
                rs.getObject("course_id") != null ? UUID.fromString(rs.getString("course_id")) : null,
                rs.getObject("user_id") != null ? UUID.fromString(rs.getString("user_id")) : null,
                rs.getObject("online_course_id") != null ? UUID.fromString(rs.getString("online_course_id")) : null
        );
    }
}
