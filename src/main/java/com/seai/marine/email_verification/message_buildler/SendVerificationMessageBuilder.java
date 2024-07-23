package com.seai.marine.email_verification.message_buildler;


import com.seai.marine.notification.email.EmailSender;
import com.seai.marine.user.contract.request.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendVerificationMessageBuilder {

    @Value("${urls.verify-token}")
    private String verifyTokenUrl;

    private final EmailSender emailSender;

    public void sendVerificationMessage(UserRegisterRequest userRegisterRequest, String email, String token) {
        String message = String.format(
                "Dear %s, <br><br>" +
                        "Thank you for registering with SeAI. To complete your registration, please confirm your email address by clicking the button below:<br><br>" +
                        "<a href=\"%s\">Verify Email</a><br><br>" +
                        "Confirmation token has 15 minutes expiration time.<br><br>" +
                        "If you did not create an account with us, please ignore this email.<br><br>" +
                        "Best regards,<br>" +
                        "The SeAI.co Team",
        userRegisterRequest.getFirstName() + " " + userRegisterRequest.getLastName(),
                verifyTokenUrl + token);
        emailSender.sendSimpleMessage(email, "Email Verification", message);
    }
}
