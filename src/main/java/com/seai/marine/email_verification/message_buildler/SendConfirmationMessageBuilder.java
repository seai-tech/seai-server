package com.seai.marine.email_verification.message_buildler;


import com.seai.marine.notification.email.EmailSender;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.email_verification.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SendConfirmationMessageBuilder {

    @Value("${urls.verify-token}")
    private String verifyTokenUrl;

    private final EmailSender emailSender;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final TemplateEngine templateEngine;


    public void sendConfirmationMessage(Optional<VerificationToken> token) {
        UserAuthentication user = userAuthenticationRepository.findById(token.get().getUserId());
        Context context = new Context();
        context.setVariable("verifyTokenUrl", verifyTokenUrl + token.get().getToken());
        String message = templateEngine.process("email_verification/confirmation_message_template.html", context);
        emailSender.sendSimpleMessage(user.getEmail(), "Welcome Aboard!", message);
    }
}
