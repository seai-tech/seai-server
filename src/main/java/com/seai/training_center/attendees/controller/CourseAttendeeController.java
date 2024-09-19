package com.seai.training_center.attendees.controller;

import com.seai.training_center.attendees.contract.request.CreateAttendeeRequest;
import com.seai.training_center.attendees.contract.request.UpdateAttendeeRequest;
import com.seai.training_center.attendees.contract.response.GetAttendeeResponse;
import com.seai.training_center.attendees.service.CourseAttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/training-centers/{trainingCenterId}/courses/{courseId}/attendees")
public class CourseAttendeeController {

    private final CourseAttendeeService courseAttendeeService;

    private static final String AUTHORIZATION = "#trainingCenterId.equals(authentication.principal.id)";

    @PostMapping
    @PreAuthorize(AUTHORIZATION)
    public void createAttendee(@RequestBody CreateAttendeeRequest createAttendeeRequest, @PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        courseAttendeeService.createAttendee(createAttendeeRequest, trainingCenterId, courseId);
    }

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public List<GetAttendeeResponse> getAttendees(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        return courseAttendeeService.getAttendees(trainingCenterId, courseId);
    }

    @PutMapping("/{attendeeId}")
    @PreAuthorize(AUTHORIZATION)
    public void updateAttendee(@RequestBody UpdateAttendeeRequest updateRequest, @PathVariable UUID trainingCenterId, @PathVariable UUID courseId, @PathVariable UUID attendeeId) {
        courseAttendeeService.updateAttendee(updateRequest, trainingCenterId, courseId, attendeeId);
    }

    @DeleteMapping("/{attendeeId}")
    @PreAuthorize(AUTHORIZATION)
    public void deleteAttendee(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId, @PathVariable UUID attendeeId) {
        courseAttendeeService.deleteAttendee(attendeeId);
    }
}
