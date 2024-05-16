package com.seai.marine.notification.controller;

import com.seai.marine.notification.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    @PutMapping("/users/{userId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void updateReminderStatus(@PathVariable UUID userId, @RequestBody boolean request) {
        reminderService.updateReminderStatus(userId, request);
    }
}
