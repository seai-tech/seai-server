package com.seai.training_center.attendees.service;

import com.seai.exception.ResourceNotFoundException;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.training_center.attendees.contract.request.CreateAttendeeRequest;
import com.seai.training_center.attendees.contract.request.UpdateAttendeeRequest;
import com.seai.training_center.attendees.mapper.AttendeeMapper;
import com.seai.training_center.attendees.model.Attendee;
import com.seai.training_center.attendees.repository.OnlineCourseAttendeeRepository;
import com.seai.training_center.online_course.model.OnlineCourse;
import com.seai.training_center.online_course.service.OnlineCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnlineCourseAttendeeService {

    private final OnlineCourseAttendeeRepository onlineCourseAttendeeRepository;

    private final OnlineCourseService onlineCourseService;

    private final AttendeeMapper attendeeMapper;

    private final UserAuthenticationRepository userAuthenticationRepository;

    @Transactional
    public Attendee createAttendee(CreateAttendeeRequest createAttendeeRequest, UUID trainingCenterId, UUID courseId) {
        OnlineCourse onlineCourse = onlineCourseService.getTrainingCenterCourseById(trainingCenterId, courseId);
        UserAuthentication user = null;
        try {
            user = userAuthenticationRepository.findByEmail(createAttendeeRequest.getEmail());
        } catch (ResourceNotFoundException ignore) {}
        Attendee attendee = attendeeMapper.map(createAttendeeRequest);
        attendee.setId(UUID.randomUUID());
        attendee.setCourseId(null);
        attendee.setUserId(user != null ? user.getId() : null);
        attendee.setOnlineCourseId(onlineCourse.getId());
        onlineCourseAttendeeRepository.save(attendee);
        return attendee;
    }

    public List<Attendee> getAttendees(UUID courseId) {
        OnlineCourse onlineCourse = onlineCourseService.getOnlineCourseById(courseId);
        return onlineCourseAttendeeRepository.findAll(onlineCourse.getId());
    }

    @Transactional
    public Attendee updateAttendee(UpdateAttendeeRequest updateRequest, UUID trainingCenterID, UUID courseId, UUID attendeeId) {
        OnlineCourse onlineCourse = onlineCourseService.getTrainingCenterCourseById(trainingCenterID, courseId);
        Optional<Attendee> existingAttendee = Optional.ofNullable(onlineCourseAttendeeRepository.findById(attendeeId).orElseThrow(() ->
                new ResourceNotFoundException("Attendee with id " + attendeeId + " not found")));
        validateAttendeeIsPartOfCourse(courseId, existingAttendee.get());
        Attendee attendee = attendeeMapper.map(updateRequest);
        attendee.setId(existingAttendee.get().getId());
        attendee.setCourseId(null);
        attendee.setUserId(existingAttendee.get().getUserId());
        attendee.setOnlineCourseId(onlineCourse.getId());
        onlineCourseAttendeeRepository.update(attendee);
        return attendee;
    }

    public void deleteAttendee(UUID attendeeId, UUID courseId) {
        onlineCourseAttendeeRepository.delete(attendeeId, courseId);
    }

    public void validateAttendeeIsPartOfCourse(UUID courseId, Attendee attendee) {
        if (attendee.getOnlineCourseId() == null || (!attendee.getOnlineCourseId().equals(courseId))) {
            throw new ResourceNotFoundException("Attendee with id: " + attendee.getId() + " is not part of course with id: " + courseId);
        }
    }
}
