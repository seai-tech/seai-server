package com.seai.marine.email_verification.message_buildler;


import com.seai.marine.notification.email.EmailSender;
import com.seai.marine.user.contract.request.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class SendVerificationMessageBuilder {

    @Value("${urls.verify-token}")
    private String verifyTokenUrl;

    @Value("{email-verification.minutes-displayed-in-message}")
    private String tokenMinutes;

    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;

    public void sendVerificationMessage(UserRegisterRequest userRegisterRequest, String email, String token) {
        Context context = new Context();
        context.setVariable("fullName", userRegisterRequest.getFirstName() + " " + userRegisterRequest.getLastName());
        context.setVariable("verifyTokenUrl", verifyTokenUrl + token);
        context.setVariable("tokenMinutes", tokenMinutes);
        String message = templateEngine.process("email_verification/verification_message_template.html", context);
        emailSender.sendSimpleMessage(email, "Email Verification", message);
    }
}
