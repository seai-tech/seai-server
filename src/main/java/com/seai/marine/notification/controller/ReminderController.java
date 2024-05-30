package com.seai.marine.notification.controller;

import com.seai.marine.notification.contract.request.UpdateReminderSubscriptionRequest;
import com.seai.marine.notification.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ReminderController {

    private final ReminderService reminderService;

    @PutMapping("/users/{userId}/reminders")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void updateReminderSubscription(@PathVariable UUID userId, @RequestBody UpdateReminderSubscriptionRequest request) {
        reminderService.updateReminderSubscription(userId, request);
    }
}
