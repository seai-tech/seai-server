package com.seai.training_center.course.service;

import com.seai.exception.MaxSeatsReachedException;
import com.seai.training_center.attendees.repository.AttendeeRepository;
import com.seai.training_center.attendees.service.AttendeeService;
import com.seai.training_center.course.contract.request.CreateCourseRequest;
import com.seai.training_center.course.contract.request.UpdateCourseRequest;
import com.seai.training_center.course.contract.response.CreateCourseResponse;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import com.seai.training_center.course.mapper.CourseMapper;
import com.seai.training_center.course.model.Course;
import com.seai.training_center.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final AttendeeRepository attendeeRepository;
    private final AttendeeService attendeeService;

    public CreateCourseResponse createCourse(@RequestBody CreateCourseRequest createCourseRequest, @PathVariable UUID trainingCenterId) {
        Course course = courseMapper.map(createCourseRequest);
        course.setId(UUID.randomUUID());
        course.setTrainingCenterId(trainingCenterId);
        return courseMapper.mapToCreateCourseResponse(courseRepository.save(course));
    }

    public List<GetCourseResponse> getAllCourses() {
        List<Course> allCourses = courseRepository.findAll();
        return allCourses.stream()
                .map(course -> {
                    GetCourseResponse courseResponse = courseMapper.map(course);
                    courseResponse.setAvailableSeats(getAvailable(course));
                    return courseResponse;
                })
                .collect(Collectors.toList());
    }

    public List<GetCourseResponse> getAllCoursesForTrainingCenter(UUID trainingCenterId) {
        List<Course> allCourses = courseRepository.findAllForTrainingCenter(trainingCenterId);
        return allCourses.stream()
                .map(course -> {
                    GetCourseResponse courseResponse = courseMapper.map(course);
                    courseResponse.setAvailableSeats(getAvailable(course));
                    return courseResponse;
                })
                .collect(Collectors.toList());
    }

    public GetCourseResponse getCourseById(UUID trainingCenterId, UUID courseId) {
        Course course = courseRepository.getCourseById(trainingCenterId, courseId);
        GetCourseResponse courseResponse = courseMapper.map(course);
        courseResponse.setAvailableSeats(getAvailable(course));
        return courseResponse;
    }

    public int getAvailable(Course course) {
        int currentAttendeeCount = attendeeRepository.countByCourseId(course.getId());
        return course.getMaxSeats() - currentAttendeeCount;
    }

    public void validateMaxSeats(GetCourseResponse course) {
        int currentAttendeeCount = attendeeRepository.countByCourseId(course.getId());
        if (currentAttendeeCount >= course.getMaxSeats()) {
            throw new MaxSeatsReachedException("Cannot add attendee. Maximum number of seats reached for course: " + course.getId());
        }
    }

    public void updateCourse(UUID trainingCenterId, UUID courseId, UpdateCourseRequest updateCourseRequest) {
        courseRepository.getCourseById(trainingCenterId, courseId);
        Course updatedCourse = courseMapper.map(updateCourseRequest);
        int currentAttendeeCount = attendeeRepository.countByCourseId(courseId);
        if (updatedCourse.getMaxSeats() < currentAttendeeCount) {
            throw new MaxSeatsReachedException("Cannot update course. Maximum number of seats is less than the current number of attendees");
        }
        courseRepository.update(trainingCenterId, courseId, updatedCourse);
    }

    public void deleteCourse(UUID trainingCenterId, UUID courseId) {
        attendeeService.deleteAllAttendees(courseId);
        courseRepository.delete(trainingCenterId, courseId);
    }

}
