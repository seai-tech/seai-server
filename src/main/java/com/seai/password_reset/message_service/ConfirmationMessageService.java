package com.seai.password_reset.message_service;

import com.seai.marine.notification.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class ConfirmationMessageService {

    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;

    public void sendConfirmationMessage(String email) {
        Context context = new Context();
        context.setVariable("email", email);
        String message = templateEngine.process("password_reset/confirmation_message_template.html", context);
        emailSender.sendSimpleMessage(email, "SeAI Password Changed", message);
    }
}
