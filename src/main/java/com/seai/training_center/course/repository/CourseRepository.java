package com.seai.training_center.course.repository;

import com.seai.marine.document.model.MarineDocument;
import com.seai.training_center.course.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CourseRepository {

    private static final String SAVE_COURSE_QUERY = "INSERT INTO courses (id, training_center_id, name, start_date, end_date, start_time, end_time, price) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_ALL_COURSES_QUERY = "SELECT id, training_center_id, name, start_date, end_date, start_time, end_time, price FROM courses";

    private final JdbcTemplate jdbcTemplate;

    public void save(Course course, UUID trainingCenterId) {
        jdbcTemplate.update(SAVE_COURSE_QUERY,
                UUID.randomUUID(),
                trainingCenterId,
                course.getName(),
                course.getStartDate(),
                course.getEndDate(),
                Time.valueOf(course.getStartTime()),
                Time.valueOf(course.getEndTime()),
                course.getPrice());
    }

    public List<Course> findAll() {
        List<Course> allCourses = jdbcTemplate.query(FIND_ALL_COURSES_QUERY, getCourseRowMapper());
        return allCourses;
    }


    private static RowMapper<Course> getCourseRowMapper() {
        return (rs, rowNum) -> new Course(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("training_center_id")),
                rs.getString("name"),
                new Date(rs.getObject("start_date", java.sql.Date.class).getTime()),
                new Date(rs.getObject("end_date", java.sql.Date.class).getTime()),
                rs.getTime("start_date").toLocalTime(),
                rs.getTime("end_date").toLocalTime(),
                rs.getObject("price", BigDecimal.class));
    }
}
