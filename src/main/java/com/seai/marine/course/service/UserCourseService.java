package com.seai.marine.course.service;

import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.user.service.UserService;
import com.seai.training_center.attendees.contract.request.CreateAttendeeRequest;
import com.seai.training_center.attendees.service.AttendeeService;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import com.seai.training_center.course.mapper.CourseMapper;
import com.seai.training_center.course.model.Course;
import com.seai.training_center.course.repository.CourseRepository;
import com.seai.training_center.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCourseService {

    private final CourseRepository courseRepository;
    private final UserService userService;
    private final CourseService courseService;
    private final AttendeeService attendeeService;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final CourseMapper courseMapper;


    public List<GetCourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    public List<GetCourseResponse> getUserCourses(UUID userId) {
        List<Course> userCourses = courseRepository.getUserCourses(userId);
        return userCourses.stream().map(course -> {
            GetCourseResponse courseResponse = courseMapper.map(course);
            courseResponse.setAvailableSeats(courseService.getAvailable(course));
            return courseResponse;
        }).collect(Collectors.toList());

    }

    public void attendToCourse(UUID userId, UUID courseId) {
        GetUserResponse user = userService.getUserById(userId);
        UserAuthentication userAuthentication = userAuthenticationRepository.findById(userId);
        GetCourseResponse course = courseService.getCourseById(courseId);
        courseService.validateMaxSeats(course);
        attendeeService.createAttendee(new CreateAttendeeRequest(user.getFirstName(), userAuthentication.getEmail(), user.getPhone(), "", true), course.getTrainingCenterId(), course.getId());
    }

    public void leaveCourse(UUID userId, UUID courseId) {
        attendeeService.deleteUser(courseId, userId);
    }
}
