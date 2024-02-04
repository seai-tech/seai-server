package com.seai.training_center.course.controller;

import com.seai.training_center.course.contract.request.CreateCourseRequest;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import com.seai.training_center.course.mapper.CourseMapper;
import com.seai.training_center.course.model.Course;
import com.seai.training_center.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class CourseController {

    private final CourseMapper courseMapper;

    private final CourseRepository courseRepository;

    @PostMapping("/training-centers/{trainingCenterId}/courses")
    public void createCourse(@RequestBody CreateCourseRequest createCourseRequest, @PathVariable UUID trainingCenterId) {
        Course course = courseMapper.map(createCourseRequest);
        courseRepository.save(course, trainingCenterId);
    }

    @GetMapping("/training-centers/courses")
    public List<GetCourseResponse> getAllCourses() {
        List<Course> allCourses = courseRepository.findAll();
        return allCourses.stream().map(courseMapper::map).toList();
    }
}
