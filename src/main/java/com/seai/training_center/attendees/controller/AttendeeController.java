package com.seai.training_center.attendees.controller;

import com.seai.training_center.attendees.contract.request.CreateAttendeeRequest;
import com.seai.training_center.attendees.contract.request.UpdateAttendeeRequest;
import com.seai.training_center.attendees.contract.response.GetAttendeeResponse;
import com.seai.training_center.attendees.service.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/training-centers/{trainingCenterId}/courses/{courseId}/attendees")
public class AttendeeController {
    private final AttendeeService attendeeService;

    @PostMapping
    public void createAttendee(@RequestBody CreateAttendeeRequest createAttendeeRequest, @PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        attendeeService.createAttendee(createAttendeeRequest, trainingCenterId, courseId);
    }

    @GetMapping
    public List<GetAttendeeResponse> getAttendees(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        return attendeeService.getAttendees(trainingCenterId, courseId);
    }

    @PutMapping("/{attendeeId}")
    public void updateAttendee(@RequestBody UpdateAttendeeRequest updateRequest, @PathVariable UUID trainingCenterId, @PathVariable UUID courseId, @PathVariable UUID attendeeId) {
        attendeeService.updateAttendee(updateRequest, trainingCenterId, courseId, attendeeId);
    }

    @DeleteMapping("/{attendeeId}")
    public void deleteAttendee(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId, @PathVariable UUID attendeeId) {
        attendeeService.deleteAttendee(attendeeId);
    }
}