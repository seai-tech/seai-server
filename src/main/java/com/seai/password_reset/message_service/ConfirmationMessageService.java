package com.seai.password_reset.message_service;

import com.seai.marine.notification.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmationMessageService {

    private final EmailSender emailSender;

    public void sendConfirmationMessage(String email) {
        String message = String.format(
                "Hello Sailor,<br><br>" +
                        "This is a confirmation that the password for your account %s has just been changed.<br><br>" +
                        "If you did not make this change, please contact our support team immediately.<br><br>" +
                        "Thank you,<br>" +
                        "The SEAI Team", email);

        emailSender.sendSimpleMessage(email, "SeAI Password Changed", message);
    }
}
