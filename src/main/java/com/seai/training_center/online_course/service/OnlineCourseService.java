package com.seai.training_center.online_course.service;

import com.seai.exception.ResourceNotFoundException;
import com.seai.training_center.attendees.repository.OnlineCourseAttendeeRepository;
import com.seai.training_center.online_course.contract.request.CreateOnlineCourseRequest;
import com.seai.training_center.online_course.contract.request.UpdateOnlineCourseRequest;
import com.seai.training_center.online_course.contract.response.CreateOnlineCourseResponse;
import com.seai.training_center.online_course.contract.response.GetOnlineCourseResponse;
import com.seai.training_center.online_course.contract.response.UpdateOnlineCourseResponse;
import com.seai.training_center.online_course.mapper.OnlineCourseMapper;
import com.seai.training_center.online_course.model.OnlineCourse;
import com.seai.training_center.online_course.repository.OnlineCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnlineCourseService {

    private final OnlineCourseRepository onlineCourseRepository;

    private final OnlineCourseMapper onlineCourseMapper;

    private final OnlineCourseAttendeeRepository onlineCourseAttendeeRepository;

    private final OnlineCourseFileService onlineCourseFileService;

    @Transactional
    public CreateOnlineCourseResponse createOnlineCourse(CreateOnlineCourseRequest createOnlineCourseRequest, UUID trainingCenterId) {
        OnlineCourse onlineCourse = onlineCourseMapper.map(createOnlineCourseRequest);
        onlineCourse.setId(UUID.randomUUID());
        onlineCourseRepository.save(onlineCourse, trainingCenterId);
        return onlineCourseMapper.mapToCreateResponse(onlineCourse);
    }

    public List<GetOnlineCourseResponse> getAllOnlineCourses (UUID trainingCenterId) {
        return onlineCourseRepository.find(trainingCenterId).stream().map(onlineCourseMapper::mapToGetResponse).toList();
    }

    public OnlineCourse getOnlineCourseById(UUID trainingCenterId, UUID courseId) throws ResourceNotFoundException {
         OnlineCourse onlineCourse = onlineCourseRepository.findById(trainingCenterId, courseId).
                orElseThrow(() -> new ResourceNotFoundException("ONLINE_COURSE_ID={" + courseId + "} not found."));
        return onlineCourse;
    }

    public UpdateOnlineCourseResponse updateCourse(UUID trainingCenterId, UUID courseId, UpdateOnlineCourseRequest updateCourseRequest) {
        OnlineCourse onlineCourse = getOnlineCourseById(trainingCenterId, courseId);
        OnlineCourse updatedCourse = onlineCourseMapper.map(updateCourseRequest);
        updatedCourse.setId(onlineCourse.getId());
        onlineCourseRepository.update(trainingCenterId, courseId, updatedCourse);
        return onlineCourseMapper.mapToUpdateResponse(updatedCourse);
    }

    @Transactional
    public void deleteCourse(UUID trainingCenterId, UUID courseId) {
        onlineCourseAttendeeRepository.deleteAll(courseId);
        onlineCourseFileService.delete(trainingCenterId, courseId);
        onlineCourseRepository.delete(trainingCenterId, courseId);
    }
}
