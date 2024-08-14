package com.seai.password_reset.message_service;

import com.seai.marine.notification.email.EmailSender;
import com.seai.password_reset.model.PasswordResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class ResetMessageService {

    @Value("${password-urls.reset-password}")
    private String resetPasswordUrl;

    @Value("${password-verification.hours-displayed-in-message}")
    private String hours;

    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;

    public void sendResetLink(PasswordResetToken token, String email) {
        Context context = new Context();
        context.setVariable("hours", hours);
        context.setVariable("resetPasswordUrl", resetPasswordUrl + token.getToken());
        String message = templateEngine.process("password_reset/reset_message_template.html", context);
        emailSender.sendSimpleMessage(email, "SeAI Password Reset Request", message);
    }
}
