package com.seai.marine.user.controller;


import com.seai.marine.user.contract.request.UserAuthenticationRequest;
import com.seai.marine.user.contract.response.UserAuthenticationResponse;
import com.seai.spring.security.model.SecurityUser;
import com.seai.spring.security.service.JwtService;
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
@RequestMapping("/api/v1/users")
public class UsersAuthController {

    private final JwtService jwtService;

    private final AuthenticationManager usersAuthenticationProvider;

    @PostMapping("/authentication")
    public UserAuthenticationResponse authenticateAndGetToken(@RequestBody @Valid UserAuthenticationRequest userAuthentaicationRequest) {
        Authentication authentication = usersAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userAuthentaicationRequest.getEmail(), userAuthentaicationRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new UserAuthenticationResponse(((SecurityUser)authentication.getPrincipal()).getId().toString(), jwtService.generateToken(userAuthentaicationRequest.getEmail()));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
