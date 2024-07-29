package com.seai.password_reset.message_service;

import com.seai.marine.notification.email.EmailSender;
import com.seai.password_reset.model.PasswordResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetMessageService {

    @Value("${password-urls.reset-password}")
    private String resetPasswordUrl;

    private final EmailSender emailSender;

    public void sendResetLink(PasswordResetToken token, String email) {
        String message = String.format("Greetings Sailor,<br><br>" +
                "We received a request to reset your password.<br><br>" +
                "This link will expire in 24 hours. You can change your password by clicking the link below:<br><br>" +
                "<a href=\"%s\">Change password</a><br><br>" +
                "If you did not request a password reset, please ignore this email or contact support if you have questions.<br><br>" +
                "Thank you,<br>The SEAI Team", resetPasswordUrl + token.getToken());

        emailSender.sendSimpleMessage(email, "SeAI Password Reset Request", message);
    }
}
