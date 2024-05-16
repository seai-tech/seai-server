package com.seai.marine.notification.service;

import com.seai.exception.ReminderStatusIsSameException;
import com.seai.marine.user.model.User;
import com.seai.marine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final UserRepository userRepository;

    public void updateReminderStatus(UUID userId, boolean request) {
        User user = userRepository.findById(userId);
        if (user.getReminder_subscription() == request) {
            throw new ReminderStatusIsSameException("Reminder status is already " + request);
        }
        user.setReminder_subscription(request);
        userRepository.updateReminderSubscription(userId, request);
    }
}
