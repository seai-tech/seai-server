package com.seai.training_center.online_course.repository;

import com.seai.exception.ResourceNotFoundException;
import com.seai.training_center.online_course.model.OnlineCourse;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGInterval;
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
            return jdbcTemplate.queryForObject(FIND_COURSE_QUERY, getCourseRowMapper(), courseId.toString(), trainingCenterId.toString());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unable to find online course with id " + courseId);
        }
    }


    public List<OnlineCourse> find(UUID trainingCenterId) {
        return jdbcTemplate.query(FIND_ALL_FOR_TRAINING_CENTER_QUERY, getCourseRowMapper(), trainingCenterId.toString());
    }

    public void save(OnlineCourse course, UUID trainingCenterId) {
        course.setPath(String.format("%s/%s/%s", trainingCenterId, course.getName(), course.getId()));
        Duration.of(0, ChronoUnit.SECONDS);
        jdbcTemplate.update(SAVE_COURSE_QUERY,
                UUID.randomUUID(),
                trainingCenterId,
                course.getName(),
                course.getDescription(),
                Optional.ofNullable(course.getDuration()).map(d-> new PGInterval(0, 0, 0, 0, 0, d.getSeconds())).orElse(null),
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
                Optional.ofNullable(rs.getObject("duration", PGInterval.class)).map(i-> Duration.of(convertPGIntervalToSeconds(i), ChronoUnit.SECONDS)).orElse(null),
                rs.getString("path"));
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
