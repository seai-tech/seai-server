package com.seai.marine.notification.service;

import com.seai.exception.ReminderSubscriptionIsSameException;
import com.seai.marine.notification.contract.request.UpdateReminderSubscriptionRequest;
import com.seai.marine.notification.model.Reminder;
import com.seai.marine.notification.model.ReminderStatus;
import com.seai.marine.notification.repository.ReminderRepository;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserAuthenticationRepository authenticationRepository;

    public void updateReminderSubscription(UUID userId, UpdateReminderSubscriptionRequest request) {
        if (request.getStatus() == ReminderStatus.ON) {
            UserAuthentication userAuthentication = authenticationRepository.findById(userId);
            turnOnReminderSubscription(userId, userAuthentication.getEmail());
        } else {
            turnOffReminderSubscription(userId);
        }
    }


    public void turnOnReminderSubscription(UUID userId, String email) {
        if (getUserReminderSubscription(userId).isPresent()) {
            throw new ReminderSubscriptionIsSameException("Reminder is already active.");
        }
        reminderRepository.saveReminder(userId, email);
    }

    public void turnOffReminderSubscription(UUID userId) {
        if (getUserReminderSubscription(userId).isEmpty()) {
            throw new ReminderSubscriptionIsSameException("Reminder subscription is already inactive.");
        }
        reminderRepository.deleteReminder(userId);
    }

    public Optional<Reminder> getUserReminderSubscription(UUID userId) {
        return reminderRepository.findReminderByUserId(userId);
    }
}
