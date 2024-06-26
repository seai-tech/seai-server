package com.seai.training_center.attendees.service;

import com.seai.exception.ResourceNotFoundException;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.user.service.UserService;
import com.seai.training_center.attendees.contract.request.CreateAttendeeRequest;
import com.seai.training_center.attendees.contract.request.UpdateAttendeeRequest;
import com.seai.training_center.attendees.contract.response.GetAttendeeResponse;
import com.seai.training_center.attendees.mapper.AttendeeMapper;
import com.seai.training_center.attendees.model.Attendee;
import com.seai.training_center.attendees.repository.AttendeeRepository;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import com.seai.training_center.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    private final AttendeeMapper attendeeMapper;

    private final CourseService courseService;

    private final UserAuthenticationRepository userAuthenticationRepository;


    public void createAttendee(CreateAttendeeRequest createAttendeeRequest, UUID trainingCenterId, UUID courseId) {
        GetCourseResponse course = courseService.getCourseById(trainingCenterId, courseId);
        courseService.validateMaxSeats(course);
        UserAuthentication user = null;
        try {
            user = userAuthenticationRepository.findByEmail(createAttendeeRequest.getEmail());
        } catch (ResourceNotFoundException ex) {
        }
        Attendee attendee = attendeeMapper.map(createAttendeeRequest);
        attendee.setId(UUID.randomUUID());
        attendee.setCourseId(courseId);
        attendee.setUserId(user != null ? user.getId() : null);
        attendeeRepository.save(attendee);
    }

    public List<GetAttendeeResponse> getAttendees (UUID trainingCenterId, UUID courseId){
        courseService.getCourseById(trainingCenterId, courseId);
        List<Attendee> allAttendees = attendeeRepository.findAll(courseId);
        return allAttendees.stream().map(attendeeMapper::map).toList();
    }

    public void updateAttendee(UpdateAttendeeRequest updateRequest, UUID trainingCenterId, UUID courseId, UUID attendeeId) {
        courseService.getCourseById(trainingCenterId, courseId);
        validateAttendee(courseId, attendeeId);
        Attendee existingAttendee = attendeeRepository.findById(attendeeId);
        Attendee attendee = attendeeMapper.map(updateRequest);
        attendee.setId(existingAttendee.getId());
        attendee.setCourseId(courseId);
        attendee.setUserId(existingAttendee.getUserId());
        attendeeRepository.update(attendee);
    }

    public void deleteAttendee(UUID attendeeId) {
        attendeeRepository.delete(attendeeId);
    }

    public void validateAttendee(UUID courseId, UUID attendeeId) {
        Attendee attendee = attendeeRepository.findById(attendeeId);
        if (attendee == null || !attendee.getCourseId().equals(courseId)) {
            throw new ResourceNotFoundException("Attendee with id: " + attendeeId + " is not part of course with id: " + courseId);
        }
    }
}
