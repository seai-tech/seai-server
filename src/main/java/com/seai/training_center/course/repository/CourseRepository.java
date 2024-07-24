package com.seai.training_center.course.repository;

import com.seai.exception.ResourceNotFoundException;
import com.seai.training_center.course.model.Course;
import com.seai.training_center.course.model.CurrencyOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CourseRepository {

    private static final String SAVE_COURSE_QUERY = "INSERT INTO courses (id, training_center_id, name, start_date, end_date, start_time, end_time, price, currency, max_seats, description, is_published) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_ALL_COURSES_QUERY = "SELECT id, training_center_id, name, start_date, end_date, start_time, end_time, price, currency, max_seats, description, is_published FROM courses";

    private static final String FIND_ALL_COURSES_FOR_TRAINING_CENTER_QUERY = "SELECT id, training_center_id, name, start_date, end_date, start_time, end_time, price, currency, max_seats, description, is_published FROM courses WHERE training_center_id=?";

    private static final String FIND_USER_COURSES = "SELECT c.id, c.training_center_id, c.name, c.start_date, c.end_date, c.start_time, c.end_time, c.price, c.currency, c.max_seats, c.description, c.is_published FROM seai.attendees a JOIN seai.courses c ON a.course_id = c.id WHERE a.user_id = ?";

    private static final String FIND_COURSE_BY_ID_QUERY = "SELECT id, training_center_id, name, start_date, end_date, start_time, end_time, price, currency, max_seats, description, is_published FROM courses WHERE id=?";

    private static final String UPDATE_COURSE_QUERY = "UPDATE courses SET name=?, start_date=?, end_date=?, start_time=?, end_time=?, price=?, currency=?, max_seats=?, description=?, is_published=? WHERE training_center_id=? AND id=?";

    private static final String DELETE_COURSE_QUERY = "DELETE FROM courses WHERE training_center_id=? AND id=?";

    private final JdbcTemplate jdbcTemplate;

    public Course save(Course course) {
        jdbcTemplate.update(SAVE_COURSE_QUERY,
                course.getId(),
                course.getTrainingCenterId(),
                course.getName(),
                course.getStartDate(),
                course.getEndDate(),
                Optional.ofNullable(course.getStartTime()).map(Time::valueOf).orElse(null),
                Optional.ofNullable(course.getEndTime()).map(Time::valueOf).orElse(null),
                course.getPrice(),
                Optional.ofNullable(course.getCurrency()).map(CurrencyOptions::name).orElse(null),
                course.getMaxSeats(),
                course.getDescription(),
                course.getIsPublished());
        return course;
    }

    public List<Course> findAll() {
        return jdbcTemplate.query(FIND_ALL_COURSES_QUERY, getCourseRowMapper());
    }

    public List<Course> findAllForTrainingCenter(UUID trainingCenterId) {
        return jdbcTemplate.query(FIND_ALL_COURSES_FOR_TRAINING_CENTER_QUERY, getCourseRowMapper(), trainingCenterId.toString());
    }

    public List<Course> getUserCourses(UUID userId) {
        return jdbcTemplate.query(FIND_USER_COURSES, getCourseRowMapper(), userId.toString());
    }


    public Course getCourseById(UUID courseId) {
        try {
            return jdbcTemplate.queryForObject(FIND_COURSE_BY_ID_QUERY, getCourseRowMapper(), courseId.toString());
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Course with id " + courseId + " not found");
        }
    }

    public void update(UUID trainingCenterId, UUID courseId, Course course) {
        jdbcTemplate.update(UPDATE_COURSE_QUERY,
                course.getName(),
                course.getStartDate(),
                course.getEndDate(),
                course.getStartTime(),
                course.getEndTime(),
                course.getPrice(),
                Optional.ofNullable(course.getCurrency()).map(CurrencyOptions::name).orElse(null),
                course.getMaxSeats(),
                course.getDescription(),
                course.getIsPublished(),
                trainingCenterId.toString(),
                courseId.toString());
    }

    public void delete(UUID trainingCenterId, UUID courseId) {
        jdbcTemplate.update(DELETE_COURSE_QUERY, trainingCenterId.toString(), courseId.toString());
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
                Optional.ofNullable(rs.getString("currency")).map(CurrencyOptions::fromName).orElse(null), rs.getInt("max_seats"),
                rs.getString("description"),
                rs.getBoolean("is_published"));
    }
}
