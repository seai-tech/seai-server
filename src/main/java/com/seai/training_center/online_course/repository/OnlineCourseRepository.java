package com.seai.training_center.online_course.repository;

import com.seai.training_center.course.model.Course;
import com.seai.training_center.course.model.CurrencyOptions;
import com.seai.training_center.online_course.model.OnlineCourse;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGInterval;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OnlineCourseRepository {

    private static final String SAVE_COURSE_QUERY = "INSERT INTO online_courses (id, training_center_id, name, description, path, duration) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String FIND_ALL_COURSES_QUERY = "SELECT id, training_center_id, name, description, path, duration FROM online_courses";

    private static final String FIND_COURSE_QUERY = "SELECT id, training_center_id, name, description, path, duration FROM online_courses WHERE id=?";

    private static final String FIND_TRAINING_CENTER_COURSE_QUERY = "SELECT id, training_center_id, name, description, path, duration FROM online_courses WHERE id=? AND training_center_id=?";

    private static final String FIND_ALL_FOR_TRAINING_CENTER_QUERY = "SELECT id, training_center_id, name, description, path, duration FROM online_courses WHERE training_center_id=?";

    private static final String UPDATE_COURSE_QUERY = "UPDATE online_courses SET name=?, description=?,  path=?, duration=? WHERE training_center_id=? AND id=?";

    private static final String DELETE_COURSE_QUERY = "DELETE FROM online_courses WHERE id=? AND training_center_id =?";

    private static final String FIND_USER_COURSES = "SELECT c.id, c.training_center_id, c.name, c.description, c.path, c.duration FROM seai.attendees a JOIN seai.online_courses c ON a.online_course_id = c.id WHERE a.user_id = ?";

    private final JdbcTemplate jdbcTemplate;


    public Optional<OnlineCourse> findById(UUID courseId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_COURSE_QUERY,
                    getCourseRowMapper(),
                    courseId.toString()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<OnlineCourse> findTrainingCenterCourseById(UUID trainingCenterId, UUID courseId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_TRAINING_CENTER_COURSE_QUERY,
                    getCourseRowMapper(),
                    trainingCenterId.toString(),
                    courseId.toString()
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OnlineCourse> find(UUID trainingCenterId) {
        return jdbcTemplate.query(FIND_ALL_FOR_TRAINING_CENTER_QUERY,
                getCourseRowMapper(),
                trainingCenterId.toString());
    }

    public List<OnlineCourse> getUserCourses(UUID userId) {
        return jdbcTemplate.query(FIND_USER_COURSES, getCourseRowMapper(), userId.toString());
    }

    public void save(OnlineCourse course, UUID trainingCenterId) {
        course.setPath(String.format("%s/%s/%s", trainingCenterId, course.getName(), course.getId()));
        Duration.of(0, ChronoUnit.SECONDS);
        jdbcTemplate.update(SAVE_COURSE_QUERY,
                course.getId(),
                trainingCenterId,
                course.getName(),
                course.getDescription(),
                course.getPath(),
                Optional.ofNullable(course.getDuration()).map(d-> new PGInterval(0, 0, 0, 0, 0, d.getSeconds())).orElse(null));
    }

    public void update(UUID trainingCenterId, UUID courseId, OnlineCourse course) {
        jdbcTemplate.update(UPDATE_COURSE_QUERY,
                course.getName(),
                course.getDescription(),
                course.getPath(),
                Optional.ofNullable(course.getDuration()).map(d-> new PGInterval(0, 0, 0, 0, 0, d.getSeconds())).orElse(null),
                trainingCenterId.toString(),
                courseId.toString());
    }

    public void delete(UUID trainingCenterId, UUID courseId) {
        jdbcTemplate.update(DELETE_COURSE_QUERY,
                courseId.toString(),
                trainingCenterId.toString()
        );
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
                rs.getString("path"),
                Optional.ofNullable(rs.getObject("duration", PGInterval.class)).map(i-> Duration.of(convertPGIntervalToSeconds(i), ChronoUnit.SECONDS)).orElse(null)
        );
    }

    private static long convertPGIntervalToSeconds(PGInterval pgInterval) {
        // Calculate total duration in seconds
        long totalSeconds = (long) pgInterval.getSeconds();
        totalSeconds += pgInterval.getMinutes() * 60L;
        totalSeconds += pgInterval.getHours() * 3600L;
        totalSeconds += (long) pgInterval.getDays() * 24 * 3600;
        totalSeconds += (long) pgInterval.getMonths() * 30 * 24 * 3600;
        totalSeconds += (long) pgInterval.getYears() * 365 * 24 * 3600;

        return totalSeconds;
    }
}
