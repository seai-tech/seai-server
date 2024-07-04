package com.seai.marine.user.controller;


import com.seai.marine.user.contract.request.UserAuthenticationRequest;
import com.seai.marine.user.contract.response.UserAuthenticationResponse;
import com.seai.marine.user.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/authentication")
    public UserAuthenticationResponse authenticateAndGetToken(@RequestBody @Valid UserAuthenticationRequest userAuthenticationRequest) {
        return userAuthService.authenticateAndGetToken(userAuthenticationRequest);
    }
}
