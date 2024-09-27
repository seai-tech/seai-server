package com.seai.marine.course.controller;


import com.seai.marine.course.service.UserOnlineCourseService;
import com.seai.training_center.online_course.model.OnlineCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/{userId}/online-courses")
@RequiredArgsConstructor
public class UserOnlineCourseController {

    private final UserOnlineCourseService userOnlineCourseService;

    private final String AUTHORIZATION = "#userId.equals(authentication.principal.id)";

    @GetMapping("/all")
    @PreAuthorize(AUTHORIZATION)
    public List<OnlineCourse> getAllCourses(@PathVariable UUID userId) {
        return userOnlineCourseService.getAllCourses();
    }

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public List<OnlineCourse> getUserCourses(@PathVariable UUID userId) {
        return userOnlineCourseService.getUserCourses(userId);
    }

    @PostMapping("/{courseId}")
    @PreAuthorize(AUTHORIZATION)
    public void attendToCourse(@PathVariable UUID userId, @PathVariable UUID courseId) {
        userOnlineCourseService.attendToCourse(userId, courseId);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize(AUTHORIZATION)
    public void leaveCourse(@PathVariable UUID userId, @PathVariable UUID courseId) {
        userOnlineCourseService.leaveCourse(userId, courseId);
    }

}
