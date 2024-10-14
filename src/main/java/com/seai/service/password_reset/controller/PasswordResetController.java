package com.seai.service.password_reset.controller;

import com.seai.service.password_reset.contract.request.ForgotPasswordRequest;
import com.seai.service.password_reset.contract.request.ResetPasswordRequest;
import com.seai.service.password_reset.contract.response.ResetPasswordResponse;
import com.seai.service.password_reset.service.PasswordResetService;
import com.seai.service.password_reset.contract.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/users/")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;


    @PostMapping("/forgot-password")
    public CompletableFuture<ResetPasswordResponse> requestReset(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return passwordResetService.createPasswordResetToken(resetPasswordRequest.getEmail());
    }

    @PatchMapping("/reset-password")
    public CompletableFuture<ResetPasswordResponse> resetPassword(@RequestParam String token, @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return passwordResetService.resetPassword(token, forgotPasswordRequest);
    }

    @PatchMapping("{userId}/change-password")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public ResetPasswordResponse changePassword(@PathVariable UUID userId, @RequestBody ChangePasswordRequest changePasswordRequest) {
        return passwordResetService.changePassword(userId, changePasswordRequest);
    }

}