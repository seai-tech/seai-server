package com.seai.marine.email_verification.message_buildler;


import com.seai.marine.notification.email.EmailSender;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.email_verification.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SendConfirmationMessageBuilder {

    @Value("${urls.verify-token}")
    private String verifyTokenUrl;

    private final EmailSender emailSender;

    private final UserAuthenticationRepository userAuthenticationRepository;

    public void sendConfirmationMessage(Optional<VerificationToken> token) {
        UserAuthentication user = userAuthenticationRepository.findById(token.get().getUserId());
        String message = String.format(
                "Ahoy, Sailor!<br><br>" +
                        "Congratulations! Your email has been successfully verified and your account is now ready for smooth sailing.<br><br>" +
                        "We're thrilled to have you aboard the SeAI crew. Here are some tips to get you started:<br><br>" +
                        "Explore the features: Navigate through our platform and discover all the awesome features we have to offer.<br>" +
                        "Stay updated: Keep an eye on your inbox for the latest updates and exciting news.<br>" +
                        "If you have any questions or need assistance, our support team is here to help. Just send us an email at support@seai.co.<br><br>" +
                        "Wishing you calm seas and prosperous voyages!<br><br>" +
                        "Best regards,<br>" +
                        "The SeAI.co Crew"
        );
        emailSender.sendSimpleMessage(user.getEmail(), "Welcome Aboard!", message);
    }
}
