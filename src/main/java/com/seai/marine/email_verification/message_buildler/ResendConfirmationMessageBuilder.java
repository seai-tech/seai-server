package com.seai.marine.email_verification.message_buildler;


import com.seai.marine.notification.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class ResendConfirmationMessageBuilder {

    @Value("${urls.verify-token}")
    private String verifyTokenUrl;

    @Value("${email-verification.minutes-displayed-in-message}")
    private String minutes;

    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;


    public void resendConfirmationMessage(String email, String token) {
        Context context = new Context();
        context.setVariable("minutes", minutes);
        context.setVariable("verifyTokenUrl", verifyTokenUrl + token);
        String message = templateEngine.process("email_verification/resend_confirm_message_template.html", context);
        emailSender.sendSimpleMessage(email, "Email Verification", message);
    }
}