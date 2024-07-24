package com.seai.marine.course.controller;


import com.seai.marine.course.service.UserCourseService;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserCourseController {

    private final UserCourseService userCourseService;

    @GetMapping("/courses")
    public List<GetCourseResponse> getAllCourses() {
        return userCourseService.getAllCourses();
    }

    @GetMapping("/{userId}/courses")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public List<GetCourseResponse> getUserCourses(@PathVariable UUID userId) {
        return userCourseService.getUserCourses(userId);
    }

    @PostMapping("/{userId}/courses/{courseId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void attendToCourse(@PathVariable UUID userId, @PathVariable UUID courseId) {
        userCourseService.attendToCourse(userId, courseId);
    }

    @DeleteMapping("/{userId}/courses/{courseId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void leaveCourse(@PathVariable UUID userId, @PathVariable UUID courseId) {
        userCourseService.leaveCourse(userId, courseId);
    }

}