package com.seai.marine.email_verification.service;

import com.seai.common.exception.TokenException;
import com.seai.marine.email_verification.contract.response.TokenConfirmedResponse;
import com.seai.marine.email_verification.message_buildler.ResendConfirmationMessageBuilder;
import com.seai.marine.email_verification.message_buildler.SendConfirmationMessageBuilder;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.email_verification.model.VerificationToken;
import com.seai.marine.email_verification.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final TokenGenerator tokenGenerator;
    private final ResendConfirmationMessageBuilder resendConfirmationMessageBuilder;
    private final SendConfirmationMessageBuilder sendConfirmationMessageBuilder;
    @Value("${urls.redirect-to-login}")
    private String redirectToLoginUrl;
    @Value("${urls.verification-error}")
    private String verificationErrorUrl;
    @Value("${urls.verification-token-resend}")
    private String verificationTokenResendUrl;
    @Value("${email-verification.token-expiry-period-minutes}")
    private Integer tokenExpiryPeriod;


    public void saveVerificationToken(VerificationToken token) {
        token.setId(UUID.randomUUID());
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiredAt(LocalDateTime.now().plusMinutes(tokenExpiryPeriod));
        emailVerificationRepository.save(token);
    }


    public Optional<VerificationToken> getVerificationToken(String token) {
        return emailVerificationRepository.findByToken(token);
    }

    @Async
    @Transactional
    public CompletableFuture<String> verifyToken(String token) {
        Optional<VerificationToken> confirmationToken = getVerificationToken(token);
        if (confirmationToken.isEmpty()) {
            return CompletableFuture.completedFuture(verificationErrorUrl + "Token not found");
        }
        if (confirmationToken.get().getConfirmedAt() != null) {
            return CompletableFuture.completedFuture(redirectToLoginUrl);
        }
        if (confirmationToken.get().getExpiredAt().isBefore(LocalDateTime.now())) {
            return CompletableFuture.completedFuture(verificationTokenResendUrl);
        }
        confirmationToken.get().setConfirmedAt(LocalDateTime.now());
        emailVerificationRepository.update(confirmationToken.get());
        sendConfirmationMessageBuilder.sendConfirmationMessage(confirmationToken);
        return CompletableFuture.completedFuture(redirectToLoginUrl);
    }


    public Optional<VerificationToken> findByUserId(UUID userId) {
        return emailVerificationRepository.findByUserId(userId);
    }


    public Optional<VerificationToken> findValidToken(UUID userId) {
        return emailVerificationRepository.findValidToken(userId, LocalDateTime.now());
    }


    public boolean isEmailConfirmed(UUID userId) {
        Optional<VerificationToken> confirmationToken = findByUserId(userId);
        return confirmationToken.isPresent() && confirmationToken.get().getConfirmedAt() != null;
    }

    @Async
    @Transactional
    public CompletableFuture<TokenConfirmedResponse> resendEmailVerificationToken(String email) {
        UserAuthentication user = userAuthenticationRepository.findByEmail(email);
        Optional<VerificationToken> existingToken = findValidToken(user.getId());
        if (isEmailConfirmed(user.getId())) {
            throw new TokenException("User is already confirmed");
        }
        if (existingToken.isPresent()) {
            throw new TokenException("There is an active token: " + existingToken.get().getToken());
        }
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUserId(user.getId());
        verificationToken.setToken(tokenGenerator.generateToken());
        saveVerificationToken(verificationToken);
        resendConfirmationMessageBuilder.resendConfirmationMessage(email, verificationToken.getToken());
        return CompletableFuture.completedFuture(new TokenConfirmedResponse("A new verification link has been sent to your email address."));
    }

    @Scheduled(cron = "${token.scheduler.cron}")
    public void deleteExpiredTokens() {
            emailVerificationRepository.deleteAllExpiredTokens(LocalDateTime.now());
        }
    }

