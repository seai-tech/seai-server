package com.seai.training_center.attendees.controller;

import com.seai.training_center.attendees.contract.request.CreateAttendeeRequest;
import com.seai.training_center.attendees.contract.request.UpdateAttendeeRequest;
import com.seai.training_center.attendees.model.Attendee;
import com.seai.training_center.attendees.service.OnlineCourseAttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/training-centers/{trainingCenterId}/online-courses/{courseId}/attendees")
public class OnlineCourseAttendeeController {

    private final OnlineCourseAttendeeService onlineCourseAttendeeService;

    private static final String AUTHORIZATION = "#trainingCenterId.equals(authentication.principal.id)";

    @PostMapping
    @PreAuthorize(AUTHORIZATION)
    public Attendee createAttendee(@RequestBody CreateAttendeeRequest createAttendeeRequest, @PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        return onlineCourseAttendeeService.createAttendee(createAttendeeRequest, trainingCenterId, courseId);
    }

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public List<Attendee> getAttendees(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        return onlineCourseAttendeeService.getAttendees(trainingCenterId, courseId);
    }

    @PutMapping("/{attendeeId}")
    @PreAuthorize(AUTHORIZATION)
    public Attendee updateAttendee(@RequestBody UpdateAttendeeRequest updateRequest, @PathVariable UUID trainingCenterId, @PathVariable UUID courseId, @PathVariable UUID attendeeId) {
        return onlineCourseAttendeeService.updateAttendee(updateRequest, trainingCenterId, courseId, attendeeId);
    }

    @DeleteMapping("/{attendeeId}")
    @PreAuthorize(AUTHORIZATION)
    public void deleteAttendee(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId, @PathVariable UUID attendeeId) {
        onlineCourseAttendeeService.deleteAttendee(attendeeId);
    }
}
