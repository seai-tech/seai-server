package com.seai.marine.email_verification.controller;

import com.seai.marine.email_verification.contract.response.TokenConfirmedResponse;
import com.seai.marine.email_verification.service.EmailVerificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify-email")
    public void verifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException, ExecutionException, InterruptedException {
        response.sendRedirect(emailVerificationService.verifyToken(token).get());
    }

    @PostMapping("/resend-email-verification-token")
    public CompletableFuture<TokenConfirmedResponse> resendEmailVerificationToken(@RequestParam String email) {
        return emailVerificationService.resendEmailVerificationToken(email);
    }
}