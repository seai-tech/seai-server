package com.seai.training_center.online_course.controller;

import com.seai.training_center.online_course.contract.request.CreateOnlineCourseRequest;
import com.seai.training_center.online_course.contract.request.UpdateOnlineCourseRequest;
import com.seai.training_center.online_course.contract.response.CreateOnlineCourseResponse;
import com.seai.training_center.online_course.contract.response.GetOnlineCourseResponse;
import com.seai.training_center.online_course.contract.response.UpdateOnlineCourseResponse;
import com.seai.training_center.online_course.mapper.OnlineCourseMapper;
import com.seai.training_center.online_course.service.OnlineCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/training-centers/{trainingCenterId}/online-courses")
public class OnlineCourseController {

    private final OnlineCourseService onlineCourseService;

    private final OnlineCourseMapper onlineCourseMapper;

    private static final String AUTHORIZATION = "#trainingCenterId.equals(authentication.principal.id)";

    @PostMapping
    @PreAuthorize(AUTHORIZATION)
    public CreateOnlineCourseResponse createOnlineCourse(@RequestBody CreateOnlineCourseRequest createOnlineCourseRequest, @PathVariable UUID trainingCenterId) {
        return onlineCourseService.createOnlineCourse(createOnlineCourseRequest, trainingCenterId);
    }

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public List<GetOnlineCourseResponse> getAllOnlineCourses(@PathVariable UUID trainingCenterId) {
        return onlineCourseService.getAllOnlineCourses(trainingCenterId);
    }

    @GetMapping("/{courseId}")
    @PreAuthorize(AUTHORIZATION)
    public GetOnlineCourseResponse getOnlineCourse(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        return onlineCourseMapper.mapToGetResponse(onlineCourseService.getOnlineCourseById(trainingCenterId, courseId));
    }

    @PutMapping("/{courseId}")
    @PreAuthorize(AUTHORIZATION)
    public UpdateOnlineCourseResponse updateCourse(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId, @RequestBody UpdateOnlineCourseRequest request) {
        return onlineCourseService.updateCourse(trainingCenterId, courseId, request);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize(AUTHORIZATION)
    public void delete(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        onlineCourseService.deleteCourse(trainingCenterId, courseId);
    }
}
