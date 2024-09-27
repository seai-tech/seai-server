package com.seai.marine.course.controller;


import com.seai.marine.course.service.UserCourseService;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/{userId}/courses")
@RequiredArgsConstructor
public class UserCourseController {

    private final UserCourseService userCourseService;

    private final String AUTHORIZATION = "#userId.equals(authentication.principal.id)";

    @GetMapping("/all")
    @PreAuthorize(AUTHORIZATION)
    public List<GetCourseResponse> getAllCourses(@PathVariable UUID userId) {
        return userCourseService.getAllCourses();
    }

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public List<GetCourseResponse> getUserCourses(@PathVariable UUID userId) {
        return userCourseService.getUserCourses(userId);
    }

    @PostMapping("/{courseId}")
    @PreAuthorize(AUTHORIZATION)
    public void attendToCourse(@PathVariable UUID userId, @PathVariable UUID courseId) {
        userCourseService.attendToCourse(userId, courseId);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize(AUTHORIZATION)
    public void leaveCourse(@PathVariable UUID userId, @PathVariable UUID courseId) {
        userCourseService.leaveCourse(userId, courseId);
    }
}
