package com.seai.marine.notification.controller;

import com.seai.marine.notification.contract.request.UpdateReminderSubscriptionRequest;
import com.seai.marine.notification.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ReminderController {

    private final ReminderService reminderService;

    @PutMapping("/users/{userId}/reminders/")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void updateReminderSubscription(@PathVariable UUID userId, @RequestBody UpdateReminderSubscriptionRequest request) {
        reminderService.updateReminderSubscription(userId, request);
    }
}
