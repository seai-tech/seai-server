package com.seai.spring.security.password_reset.controller;

import com.seai.spring.security.password_reset.contract.request.ForgotPasswordRequest;
import com.seai.spring.security.password_reset.contract.request.ChangePasswordRequest;
import com.seai.spring.security.password_reset.contract.response.ResetPasswordResponse;
import com.seai.spring.security.password_reset.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;


    @PutMapping("reset-password/request")
    public ResetPasswordResponse requestReset(@RequestParam String email) {
        return passwordResetService.createPasswordResetToken(email);
    }

    @PostMapping("/reset-password")
    public ResetPasswordResponse resetPassword(@RequestParam String token, @RequestBody ForgotPasswordRequest resetRequest) {
        return passwordResetService.forgotPassword(token, resetRequest);
    }

    @PutMapping("{userId}/change-password")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public ResetPasswordResponse changePassword(@RequestParam UUID userId, ChangePasswordRequest changePasswordRequest) {
        return passwordResetService.changePassword(userId, changePasswordRequest);
    }

    
}
