package com.seai.marine.email_verification.controller;

import com.seai.marine.email_verification.contract.response.TokenConfirmedResponse;
import com.seai.marine.email_verification.service.EmailVerificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify-email")
    public void verifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        response.sendRedirect(emailVerificationService.VerifyToken(token));
    }

    @PostMapping("/resend-email-verification-token")
    public TokenConfirmedResponse resendEmailVerificationToken(@RequestParam String email) {
        return emailVerificationService.resendEmailVerificationToken(email);
    }
}