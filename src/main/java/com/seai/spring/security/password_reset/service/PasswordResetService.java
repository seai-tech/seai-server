package com.seai.spring.security.password_reset.service;

import com.seai.exception.DuplicatedResourceException;
import com.seai.marine.notification.email.EmailSender;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.spring.security.password_reset.contract.request.ForgotPasswordRequest;
import com.seai.spring.security.password_reset.contract.request.ChangePasswordRequest;
import com.seai.spring.security.password_reset.contract.response.ResetPasswordResponse;
import com.seai.spring.security.password_reset.model.PasswordResetToken;
import com.seai.spring.security.password_reset.repository.PasswordResetRepository;
import com.seai.spring.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final JwtService jwtService;
    private final PasswordResetRepository passwordResetRepository;


    public ResetPasswordResponse createPasswordResetToken(String email) {
        UserAuthentication user = userAuthenticationRepository.findByEmail(email);
        checkIfCurrentUserIsAuthorized(email);
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setId(UUID.randomUUID());
        passwordResetToken.setUserId(user.getId());
        passwordResetToken.setToken(jwtService.generateToken(user.getEmail()));
        passwordResetToken.setCreatedAt(LocalDateTime.now());
        passwordResetToken.setExpiredAt(LocalDateTime.now().plusDays(1));
        passwordResetRepository.save(passwordResetToken);
        String linkTemplate = "http://localhost:8080/api/v1/users/change-password?token=%s";
        String resetLink = String.format(linkTemplate, passwordResetToken.getToken());
        String message = String.format("Greetings Sailor,\n\nWe received a request to reset your password. " +
                "This link will expire in 24 hours. You can change your password by clicking the link below:\n%s\n\nIf you did not request a password reset, please ignore this email or contact support if you have questions.\n\nThank you,\nThe SEAI Team", resetLink);
        emailSender.sendSimpleMessage(user.getEmail(), "SeAI Password Reset Request", message);
        return new ResetPasswordResponse("A reset link has been sent to your email");
    }


    public ResetPasswordResponse forgotPassword(String token, ForgotPasswordRequest forgotPasswordRequest) {
        PasswordResetToken passwordResetToken = passwordResetRepository.findByToken(token);
        UserAuthentication userAuthentication = userAuthenticationRepository.findByEmail(jwtService.extractUsername(token));
        if (passwordResetToken == null)
            throw new DuplicatedResourceException("Invalid token"); // will change to TokenException from emailVerification
        if (passwordResetToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new DuplicatedResourceException("Token has expired"); // will change to  TokenException from emailVerification
        }
        if (!Objects.equals(forgotPasswordRequest.getNewPassword(), forgotPasswordRequest.getConfirmPassword())) {
            throw new DuplicatedResourceException("Passwords do not match"); // will change to  TokenException from emailVerification
        }
        userAuthentication.setPassword(forgotPasswordRequest.getNewPassword());
        userAuthenticationRepository.update(userAuthentication);
        passwordResetRepository.delete(userAuthentication.getId());
        sendPasswordChangeConfirmation(userAuthentication.getEmail());
        return new ResetPasswordResponse("Password has been reset successfully");
    }

    public ResetPasswordResponse changePassword(UUID userId, ChangePasswordRequest request) {
        UserAuthentication userAuthentication = userAuthenticationRepository.findById(userId);
        validatePasswordChange(request, userAuthentication);
        userAuthentication.setPassword(request.getNewPassword());
        userAuthenticationRepository.update(userAuthentication);
        sendPasswordChangeConfirmation(userAuthentication.getEmail());
        return new ResetPasswordResponse("Password has been changed successfully");
    }

    public void validatePasswordChange(ChangePasswordRequest changePasswordRequest, UserAuthentication userAuthentication) {
        if (!Objects.equals(changePasswordRequest.getNewPassword(), changePasswordRequest.getConfirmPassword())) {
            throw new DuplicatedResourceException("Passwords do not match"); // will change to TokenException from emailVerification
        }
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), userAuthentication.getPassword())) {
            throw new DuplicatedResourceException("Old password is incorrect"); // will change to TokenException from emailVerification
        }
    }

    private void sendPasswordChangeConfirmation(String email) {
        String message = String.format("Hello Sailor,\n\nThis is a confirmation that the password for your account %s has just been changed.\n\nIf you did not make this change, please contact our support team immediately.\n\nThank you,\nThe SEAI Team", email);
        emailSender.sendSimpleMessage(email, "SeAI Password Changed", message);
    }


    private void checkIfCurrentUserIsAuthorized(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String currentUserEmail = ((UserDetails) principal).getUsername();
                if (!currentUserEmail.equals(email)) {
                    throw new AccessDeniedException("You are not authorized to request a password reset for this account.");
                }
            }
        }
    }
}
