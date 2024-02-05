package com.seai.training_center.online_course.controller;

import com.seai.training_center.online_course.contract.request.CreateOnlineCourseRequest;
import com.seai.training_center.online_course.contract.response.GetOnlineCourseResponse;
import com.seai.training_center.online_course.mapper.OnlineCourseMapper;
import com.seai.training_center.online_course.model.OnlineCourse;
import com.seai.training_center.online_course.repository.OnlineCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/training-centers")
public class OnlineCourseController {
    private final OnlineCourseRepository onlineCourseRepository;

    private final OnlineCourseMapper onlineCourseMapper;

    @PostMapping("/{trainingCenterId}/online-courses")
    public void createOnlineCourse(@RequestBody CreateOnlineCourseRequest createOnlineCourseRequest, @PathVariable UUID trainingCenterId) {
        OnlineCourse onlineCourse = onlineCourseMapper.map(createOnlineCourseRequest);
        onlineCourseRepository.save(onlineCourse, trainingCenterId);
    }

    @GetMapping("/{trainingCenterId}/online-courses")
    public List<GetOnlineCourseResponse> getAllOnlineCourses(@PathVariable UUID trainingCenterId) {
        return onlineCourseRepository.find(trainingCenterId).stream().map(onlineCourseMapper::map).toList();
    }


    @GetMapping("/{trainingCenterId}/online-courses/{courseId}")
    public GetOnlineCourseResponse getOnlineCourse(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        return onlineCourseMapper.map(onlineCourseRepository.find(trainingCenterId, courseId));
    }
}
