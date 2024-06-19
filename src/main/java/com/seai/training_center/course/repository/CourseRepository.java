package com.seai.training_center.course.repository;

import com.seai.exception.ResourceNotFoundException;
import com.seai.training_center.course.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CourseRepository {

    private static final String SAVE_COURSE_QUERY = "INSERT INTO courses (id, training_center_id, name, start_date, end_date, start_time, end_time, price, currency, max_seats, description, is_published) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_ALL_COURSES_QUERY = "SELECT id, training_center_id, name, start_date, end_date, start_time, end_time, price, currency, max_seats, description, is_published FROM courses";

    private static final String FIND_COURSE_BY_ID_QUERY = "SELECT id, training_center_id, name, start_date, end_date, start_time, end_time, price, currency, max_seats, description, is_published FROM courses WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    public void save(Course course, UUID trainingCenterId) {
        jdbcTemplate.update(SAVE_COURSE_QUERY,
                UUID.randomUUID(),
                trainingCenterId,
                course.getName(),
                course.getStartDate(),
                course.getEndDate(),
                Optional.ofNullable(course.getStartTime()).map(Time::valueOf).orElse(null),
                Optional.ofNullable(course.getEndTime()).map(Time::valueOf).orElse(null),
                course.getPrice(),
                Optional.ofNullable(course.getCurrency()).map(Currency::getCurrencyCode).orElse(null),
                course.getMaxSeats(),
                course.getDescription(),
                course.getIsPublished());
    }

    public List<Course> findAll() {
        return jdbcTemplate.query(FIND_ALL_COURSES_QUERY, getCourseRowMapper());
    }

    public Course getCourseById(UUID courseId) {
        try {
            return jdbcTemplate.queryForObject(FIND_COURSE_BY_ID_QUERY, getCourseRowMapper(), courseId.toString());
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Course with id " + courseId + " not found");
        }
    }

    private static RowMapper<Course> getCourseRowMapper() {
        return (rs, rowNum) -> new Course(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("training_center_id")),
                rs.getString("name"),
                new Date(rs.getObject("start_date", java.sql.Date.class).getTime()),
                new Date(rs.getObject("end_date", java.sql.Date.class).getTime()),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime(),
                rs.getObject("price", BigDecimal.class),
                Optional.ofNullable(rs.getString("currency")).map(Currency::getInstance).orElse(null),
                rs.getInt("max_seats"),
                rs.getString("description"),
                rs.getBoolean("is_published"));
    }
}
