package com.seai.training_center.course.service;

import com.seai.exception.MaxSeatsReachedException;
import com.seai.exception.ResourceNotFoundException;
import com.seai.training_center.attendees.repository.AttendeeRepository;
import com.seai.training_center.course.contract.request.CreateCourseRequest;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import com.seai.training_center.course.mapper.CourseMapper;
import com.seai.training_center.course.model.Course;
import com.seai.training_center.course.repository.CourseRepository;
import com.seai.training_center.training_center.repository.TrainingCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
        private final AttendeeRepository attendeeRepository;

    public void createCourse(@RequestBody CreateCourseRequest createCourseRequest, @PathVariable UUID trainingCenterId) {
        Course course = courseMapper.map(createCourseRequest);
        courseRepository.save(course, trainingCenterId);
    }

    public List<GetCourseResponse> getAllCourses() {
        List<Course> allCourses = courseRepository.findAll();
        return allCourses.stream().map(courseMapper::map).toList();
    }

    public List<GetCourseResponse> getAllCoursesForTrainingCenter(UUID trainingCenterId) {
        List<Course> allCourses = courseRepository.findAllForTrainingCenter(trainingCenterId);
        return allCourses.stream().map(courseMapper::map).toList();
    }

    public GetCourseResponse getCourseById(UUID trainingCenterId, UUID courseId) {
        Course course = courseRepository.getCourseById(trainingCenterId, courseId);
        return courseMapper.map(course);
    }


    public void validateMaxSeats(GetCourseResponse course) {
        int currentAttendeeCount = attendeeRepository.countByCourseId(course.getId());
        if (currentAttendeeCount >= course.getMaxSeats()) {
            throw new MaxSeatsReachedException("Cannot add attendee. Maximum number of seats reached for course: " + course.getId());
        }
    }

}
