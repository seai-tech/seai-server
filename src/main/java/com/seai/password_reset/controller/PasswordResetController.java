package com.seai.password_reset.controller;

import com.seai.password_reset.contract.request.ForgotPasswordRequest;
import com.seai.password_reset.contract.response.ResetPasswordResponse;
import com.seai.password_reset.service.PasswordResetService;
import com.seai.password_reset.contract.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;


    @PostMapping("/forgot-password")
    public ResetPasswordResponse requestReset(@RequestBody String email) {
        return passwordResetService.createPasswordResetToken(email);
    }

    @PatchMapping("/reset-password")
    public ResetPasswordResponse resetPassword(@RequestParam String token, @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return passwordResetService.resetPassword(token, forgotPasswordRequest);
    }

    @PatchMapping("{userId}/change-password")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public ResetPasswordResponse changePassword(@PathVariable UUID userId, @RequestBody ChangePasswordRequest changePasswordRequest) {
        return passwordResetService.changePassword(userId, changePasswordRequest);
    }

}