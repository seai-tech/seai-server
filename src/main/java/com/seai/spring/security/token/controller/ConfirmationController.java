package com.seai.spring.security.token.controller;

import com.seai.spring.security.token.contract.response.TokenConfirmedResponse;
import com.seai.spring.security.token.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class ConfirmationController {

    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping("/confirm")
    public TokenConfirmedResponse confirm(@RequestParam("token") String token) {
        return confirmationTokenService.confirmToken(token);
    }

    @PostMapping("/resend-confirmation")
    public TokenConfirmedResponse resendConfirmationToken(@RequestParam String email) {
        return confirmationTokenService.resendConfirmationToken(email);
    }
}