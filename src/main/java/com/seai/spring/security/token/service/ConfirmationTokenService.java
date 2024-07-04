package com.seai.spring.security.token.service;

import com.seai.exception.*;
import com.seai.marine.notification.email.EmailSender;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.spring.security.service.JwtService;
import com.seai.spring.security.token.contract.response.TokenConfirmedResponse;
import com.seai.spring.security.token.model.ConfirmationToken;
import com.seai.spring.security.token.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final JwtService jwtService;
    private final EmailSender emailSender;


    public void saveConfirmationToken(ConfirmationToken token) {
        token.setId(UUID.randomUUID());
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiredAt(LocalDateTime.now().plusMinutes(15));
        confirmationTokenRepository.save(token);
    }

    public ConfirmationToken getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void delete(UUID tokenId) {
        confirmationTokenRepository.delete(tokenId);
    }

    public TokenConfirmedResponse confirmToken(String token) {
        ConfirmationToken confirmationToken = getConfirmationToken(token);
        if (confirmationToken == null) {
            throw new ConfirmationException("Invalid token");
        }
        else if (confirmationToken.getConfirmedAt() != null) {
            throw new UserAlreadyConfirmedException("Token already confirmed");
        };
        if (confirmationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ConfirmationException("Token has expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.update(confirmationToken);
        return new TokenConfirmedResponse("Email confirmed successfully");
    }

    public Optional<ConfirmationToken> findByUserId(UUID userId) {
        return confirmationTokenRepository.findByUserId(userId);
    }

    public ConfirmationToken findValidTokens(UUID userId) {
        return confirmationTokenRepository.findValidTokens(userId, LocalDateTime.now());
    }

    public boolean isEmailConfirmed(UUID userId) {
        Optional<ConfirmationToken> confirmationToken = findByUserId(userId);
        return confirmationToken.isPresent() && confirmationToken.get().getConfirmedAt() != null;
    }

    public TokenConfirmedResponse resendConfirmationToken(String email) {
        UserAuthentication user = userAuthenticationRepository.findByEmail(email);
        ConfirmationToken existingToken = findValidTokens(user.getId());
        if (isEmailConfirmed(user.getId())) {
            throw new TokenException("User is already confirmed");
        }
        if (existingToken != null) {
            throw new TokenException("There is an active token: " + existingToken.getToken());
        }
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUserId(user.getId());
        confirmationToken.setToken(jwtService.generateToken(email));
        saveConfirmationToken(confirmationToken);
        String confirmationLink = "http://localhost:8080/api/v1/users/confirm?token=" + confirmationToken.getToken();
        String subject = "Email Verification";
        String message = String.format(
                "Dear %s,\n\n" +
                        "You requested a new confirmation link. To complete your registration, please confirm your email address within the next 15 minutes by clicking the button below:\n\n" +
                        "%s\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "The SeAI Team",
                user.getEmail(),
                confirmationLink
        );
        emailSender.sendSimpleMessage(email, subject, message);
        return new TokenConfirmedResponse("A new confirmation token has been sent to your email.");
    }

    @Scheduled(cron = "${token.scheduler.cron}")
    public void deleteExpiredTokens() {
        List<ConfirmationToken> expiredTokens = confirmationTokenRepository.findAllExpiredTokens(LocalDateTime.now());
        for (ConfirmationToken token : expiredTokens) {
            confirmationTokenRepository.delete(token.getId());
        }
    }

}
