package com.seai.marine.email_verification.message_buildler;


import com.seai.marine.notification.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResendConfirmationMessageBuilder {

    @Value("${urls.verify-token}")
    private String verifyTokenUrl;

    @Value("${email-verification.minutes-displayed-in-message}")
    private String minutes;

    private final EmailSender emailSender;

    public void resendConfirmationMessage(String email, String token) {
        String url = verifyTokenUrl + token;
        String message = String.format(
                "Dear Sailor,<br><br>" +
                        "You requested a new confirmation link. To complete your registration, please confirm your email address within the next %s minutes by clicking the button below:<br><br>" +
                        "<a href=\"%s\">Verify Email</a><br><br>" +
                        "If you did not request this, please ignore this email.<br><br>" +
                        "Best regards,<br>" +
                        "The SeAI Team",
                minutes, url
        );
        emailSender.sendSimpleMessage(email, "Email Verification", message);
    }
}