package com.seai.controller;


import com.seai.request.AuthRequest;
import com.seai.request.UserRegisterRequest;
import com.seai.response.AuthResponse;
import com.seai.security.model.SecurityUser;
import com.seai.security.service.JwtService;
import com.seai.security.service.SecurityUserService;
import com.seai.domain.model.SeaiUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final SecurityUserService securityUserService;

    @PostMapping("/login")
    public AuthResponse authenticateAndGetToken(@RequestBody @Valid AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new AuthResponse(((SecurityUser)authentication.getPrincipal()).getId().toString(), jwtService.generateToken(authRequest.getEmail()));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid UserRegisterRequest authRequest) {
        securityUserService.save(new SeaiUser(authRequest.getEmail(), authRequest.getPassword(), authRequest.getFirstName(), authRequest.getLastName()));
    }
}
