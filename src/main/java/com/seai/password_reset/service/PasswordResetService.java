package com.seai.password_reset.service;

import com.seai.exception.PasswordException;
import com.seai.exception.TokenException;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.password_reset.PasswordResetTokenGenerator;
import com.seai.password_reset.contract.request.ForgotPasswordRequest;
import com.seai.password_reset.contract.response.ResetPasswordResponse;
import com.seai.password_reset.message_service.ConfirmationMessageService;
import com.seai.password_reset.message_service.ResetMessageService;
import com.seai.password_reset.model.PasswordResetToken;
import com.seai.password_reset.contract.request.ChangePasswordRequest;
import com.seai.password_reset.repository.PasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetService {
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetRepository passwordResetRepository;
    private final ResetMessageService resetMessageService;
    private final ConfirmationMessageService confirmationMessageService;
    private final PasswordResetTokenGenerator tokenGenerator;
    @Value("${password-verification.token-expiry-period-hours}")
    private Integer tokenExpiryPeriod;


    @Async
    public CompletableFuture<ResetPasswordResponse> createPasswordResetToken(String email) {
        UserAuthentication user = userAuthenticationRepository.findByEmail(email);
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setId(UUID.randomUUID());
        passwordResetToken.setUserId(user.getId());
        passwordResetToken.setToken(tokenGenerator.generateToken(user.getEmail()));
        passwordResetToken.setCreatedAt(LocalDateTime.now());
        passwordResetToken.setExpiredAt(LocalDateTime.now().plusHours(tokenExpiryPeriod));
        passwordResetRepository.save(passwordResetToken);
        resetMessageService.sendResetLink(passwordResetToken, email);
        return CompletableFuture.completedFuture(new ResetPasswordResponse("Reset link has been sent to your email."));
    }

    @Async
    public CompletableFuture<ResetPasswordResponse> resetPassword(String token, ForgotPasswordRequest forgotPasswordRequest) {
        PasswordResetToken passwordResetToken = passwordResetRepository.findByToken(token);
        UserAuthentication userAuthentication = userAuthenticationRepository.findByEmail(tokenGenerator.extractEmail(token));
        if (passwordResetToken == null) {
            throw new TokenException("Invalid token");
        }
        if (passwordResetToken.isExpired()) {
            throw new TokenException("Token has expired");
        }
        if (!forgotPasswordRequest.arePasswordsMatching()) {
            throw new PasswordException("Passwords do not match");
        }
        userAuthentication.setPassword(forgotPasswordRequest.getNewPassword());
        userAuthenticationRepository.updateUserPassword(userAuthentication);
        confirmationMessageService.sendConfirmationMessage(userAuthentication.getEmail());
        return CompletableFuture.completedFuture(new ResetPasswordResponse("Your password has been reset successfully."));
    }

    public ResetPasswordResponse changePassword(UUID userId, ChangePasswordRequest request) {
        UserAuthentication userAuthentication = userAuthenticationRepository.findById(userId);
        validatePasswordChange(request, userAuthentication);
        userAuthentication.setPassword(request.getNewPassword());
        userAuthenticationRepository.updateUserPassword(userAuthentication);
        CompletableFuture.runAsync(() -> confirmationMessageService.sendConfirmationMessage(userAuthentication.getEmail()));
        return new ResetPasswordResponse("Password has been changed successfully");
    }


    public void validatePasswordChange(ChangePasswordRequest changePasswordRequest, UserAuthentication userAuthentication) {
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), userAuthentication.getPassword())) {
            throw new PasswordException("Old password is incorrect");
        }
        if (!changePasswordRequest.arePasswordsMatching()) {
            throw new PasswordException("Passwords do not match");
        }
    }

    @Scheduled(cron = "${password-token-delete.scheduler.cron}")
    public void deleteExpiredTokens() {
        passwordResetRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}