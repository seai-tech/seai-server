package com.seai.user.controller;


import com.seai.spring.security.model.SecurityUser;
import com.seai.spring.security.service.JwtService;
import com.seai.user.contract.request.UserAuthentaicationRequest;
import com.seai.user.contract.request.UserRegisterRequest;
import com.seai.user.mapper.UserAuthenticationMapper;
import com.seai.user.model.User;
import com.seai.user.model.UserAuthentication;
import com.seai.user.repository.UserAuthenticationRepository;
import com.seai.user.repository.UserRepository;
import com.seai.user.contract.response.AuthResponse;
import com.seai.user.mapper.UserMapper;
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

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class AuthController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserAuthenticationRepository userAuthenticationRepository;

    private final UserAuthenticationMapper userAuthenticationMapper;

    @PostMapping("/login")
    public AuthResponse authenticateAndGetToken(@RequestBody @Valid UserAuthentaicationRequest userAuthentaicationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthentaicationRequest.getEmail(), userAuthentaicationRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new AuthResponse(((SecurityUser)authentication.getPrincipal()).getId().toString(), jwtService.generateToken(userAuthentaicationRequest.getEmail()));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        UUID id = UUID.randomUUID();
        UserAuthentication userAuthentication = userAuthenticationMapper.map(userRegisterRequest, id);
        userAuthenticationRepository.save(userAuthentication);
        User user = userMapper.map(userRegisterRequest, id);
        userRepository.save(user);
    }
}
