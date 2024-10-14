package com.seai.training_center.attendees.service;

import com.seai.exception.ResourceNotFoundException;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.training_center.attendees.contract.request.CreateAttendeeRequest;
import com.seai.training_center.attendees.contract.request.UpdateAttendeeRequest;
import com.seai.training_center.attendees.contract.response.GetAttendeeResponse;
import com.seai.training_center.attendees.mapper.AttendeeMapper;
import com.seai.training_center.attendees.model.Attendee;
import com.seai.training_center.attendees.repository.CourseAttendeeRepository;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import com.seai.training_center.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseAttendeeService {

    private final CourseAttendeeRepository courseAttendeeRepository;

    private final AttendeeMapper attendeeMapper;

    private final CourseService courseService;

    private final UserAuthenticationRepository userAuthenticationRepository;


    public void createAttendee(CreateAttendeeRequest createAttendeeRequest, UUID trainingCenterId, UUID courseId) {
        GetCourseResponse course = courseService.getValidatedCourse(trainingCenterId, courseId);
        courseService.validateMaxSeats(course);
        UserAuthentication user = null;
        try {
            user = userAuthenticationRepository.findByEmail(createAttendeeRequest.getEmail());
        } catch (ResourceNotFoundException ignore) {}
        Attendee attendee = attendeeMapper.map(createAttendeeRequest);
        attendee.setId(UUID.randomUUID());
        attendee.setCourseId(courseId);
        attendee.setUserId(user != null ? user.getId() : null);
        attendee.setOnlineCourseId(null);
        courseAttendeeRepository.save(attendee);
    }

    public List<GetAttendeeResponse> getAttendees(UUID trainingCenterId, UUID courseId) {
        courseService.getValidatedCourse(trainingCenterId, courseId);
        List<Attendee> allAttendees = courseAttendeeRepository.findAll(courseId);
        return allAttendees.stream().map(attendeeMapper::map).toList();
    }

    public void updateAttendee(UpdateAttendeeRequest updateRequest, UUID trainingCenterId, UUID courseId, UUID attendeeId) {
        courseService.getValidatedCourse(trainingCenterId, courseId);
        Optional<Attendee> existingAttendee = Optional.ofNullable(courseAttendeeRepository.findById(attendeeId).orElseThrow(() ->
                new ResourceNotFoundException("Attendee with id " + attendeeId + " not found")));
        validateAttendeeIsPartOfCourse(courseId, attendeeId);
        Attendee attendee = attendeeMapper.map(updateRequest);
        attendee.setId(existingAttendee.get().getId());
        attendee.setCourseId(courseId);
        attendee.setUserId(existingAttendee.get().getUserId());
        attendee.setOnlineCourseId(null);
        courseAttendeeRepository.update(attendee);
    }

    public void deleteAttendee(UUID attendeeId, UUID courseId) {
        courseAttendeeRepository.delete(attendeeId, courseId);
    }

    public void deleteUser(UUID courseId, UUID userId) {
        courseAttendeeRepository.deleteByUserId(courseId, userId);
    }

    public void validateAttendeeIsPartOfCourse(UUID courseId, UUID attendeeId) {
        Optional<Attendee> attendee = courseAttendeeRepository.findById(attendeeId);
        if (attendee.isEmpty() || !attendee.get().getCourseId().equals(courseId)) {
            throw new ResourceNotFoundException("Attendee with id: " + attendeeId + " is not part of course with id: " + courseId);
        }
    }
}
