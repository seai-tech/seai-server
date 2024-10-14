package com.seai.marine.course.service;

import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.user.service.UserService;
import com.seai.training_center.attendees.contract.request.CreateAttendeeRequest;
import com.seai.training_center.attendees.service.OnlineCourseAttendeeService;
import com.seai.training_center.online_course.model.OnlineCourse;
import com.seai.training_center.online_course.repository.OnlineCourseRepository;
import com.seai.training_center.online_course.service.OnlineCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserOnlineCourseService {

    private final OnlineCourseRepository onlineCourseRepository;

    private final OnlineCourseService onlineCourseService;

    private final OnlineCourseAttendeeService courseAttendeeService;

    private final UserService userService;

    private final UserAuthenticationRepository userAuthenticationRepository;


    public List<OnlineCourse> getAllCourses() {
        return onlineCourseRepository.findAll();
    }

    public List<OnlineCourse> getUserCourses(UUID userId) {
        return onlineCourseRepository.getUserCourses(userId);
    }

    public void attendToCourse(UUID userId, UUID courseId) {
        GetUserResponse user = userService.getUserById(userId);
        UserAuthentication userAuthentication = userAuthenticationRepository.findById(userId);
        OnlineCourse course = onlineCourseService.getOnlineCourseById(courseId);
        courseAttendeeService.createAttendee(new CreateAttendeeRequest(user.getFirstName(), userAuthentication.getEmail(), user.getPhone(), "", true), course.getTrainingCenterId(), course.getId());
    }

    public void leaveCourse(UUID userId, UUID courseId) {
        courseAttendeeService.deleteAttendee(userId, courseId);
    }
}
