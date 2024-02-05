package com.seai.training_center.online_course.repository;

import com.seai.exception.ResourceNotFoundException;
import com.seai.training_center.course.model.Course;
import com.seai.training_center.online_course.model.OnlineCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.Duration;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OnlineCourseRepository {

    private UUID id;

    private String name;

    private String description;

    private Duration duration;

    private String path;

    private static final String SAVE_COURSE_QUERY = "INSERT INTO online_courses (id, training_center_id, name, description, duration, path) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String FIND_ALL_COURSES_QUERY = "SELECT id, training_center_id, name, description, duration, path FROM online_courses";

    private static final String FIND_COURSE_QUERY = "SELECT id, training_center_id, name, description, duration, path FROM online_courses WHERE id=? AND training_center_id=?";

    private static final String FIND_ALL_FOR_TRAINING_CENTER_QUERY = "SELECT id, training_center_id, name, description, duration, path FROM online_courses WHERE training_center_id=?";


    private final JdbcTemplate jdbcTemplate;


    public OnlineCourse find(UUID trainingCenterId, UUID courseId) {
        try {
            return jdbcTemplate.queryForObject(FIND_COURSE_QUERY, getCourseRowMapper(), courseId, trainingCenterId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unable to find online course with id " + courseId);
        }
    }


    public List<OnlineCourse> find(UUID trainingCenterId) {
        return jdbcTemplate.query(FIND_ALL_FOR_TRAINING_CENTER_QUERY, getCourseRowMapper(), trainingCenterId);
    }

    public void save(OnlineCourse course, UUID trainingCenterId) {
        course.setPath(String.format("%s/%s/%s", trainingCenterId, course.getName(), course.getId()));
        jdbcTemplate.update(SAVE_COURSE_QUERY,
                UUID.randomUUID(),
                trainingCenterId,
                course.getName(),
                course.getDescription(),
                course.getDuration(),
                course.getPath());
    }

    public List<OnlineCourse> findAll() {
        return jdbcTemplate.query(FIND_ALL_COURSES_QUERY, getCourseRowMapper());
    }


    private static RowMapper<OnlineCourse> getCourseRowMapper() {
        return (rs, rowNum) -> new OnlineCourse(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("training_center_id")),
                rs.getString("name"),
                rs.getString("description"),
                Optional.ofNullable(rs.getString("duration")).map(Duration::parse).orElse(null),
                rs.getString("path"));
    }
}
