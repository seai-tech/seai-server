package com.seai.training_center.course.controller;

import com.seai.training_center.course.contract.request.CreateCourseRequest;
import com.seai.training_center.course.contract.request.UpdateCourseRequest;
import com.seai.training_center.course.contract.response.CreateCourseResponse;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import com.seai.training_center.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class TrainingCenterCourseController {

    private final CourseService courseService;

    @PostMapping("/training-centers/{trainingCenterId}/courses")
    public CreateCourseResponse createCourse(@RequestBody CreateCourseRequest createCourseRequest, @PathVariable UUID trainingCenterId) {
        return courseService.createCourse(createCourseRequest, trainingCenterId);
    }

    @GetMapping("/training-centers/courses")
    public List<GetCourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/training-centers/{trainingCenterId}/courses")
    public List<GetCourseResponse> getAllCoursesForTrainingCenter(@PathVariable UUID trainingCenterId) {
        return courseService.getAllCoursesForTrainingCenter(trainingCenterId);
    }

    @GetMapping("/{trainingCenterId}/courses/{courseId}")
    public GetCourseResponse getCourse(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        return courseService.getValidatedCourse(trainingCenterId, courseId);
    }

    @PutMapping("/{trainingCenterId}/courses/{courseId}")
    public void updateCourse(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId, @RequestBody UpdateCourseRequest request) {
        courseService.updateCourse(trainingCenterId, courseId, request);
    }

    @DeleteMapping("/{trainingCenterId}/courses/{courseId}")
    public void deleteCourse(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        courseService.deleteCourse(trainingCenterId, courseId);
    }
}
