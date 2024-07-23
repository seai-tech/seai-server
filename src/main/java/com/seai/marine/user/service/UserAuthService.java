package com.seai.marine.user.service;

import com.seai.exception.ConfirmationException;
import com.seai.marine.user.contract.request.UserAuthenticationRequest;
import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.contract.response.UserAuthenticationResponse;
import com.seai.marine.user.mapper.UserAuthenticationMapper;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.email_verification.service.TokenGenerator;
import com.seai.marine.email_verification.message_buildler.SendVerificationMessageBuilder;
import com.seai.spring.security.model.SecurityUser;
import com.seai.spring.security.service.JwtService;
import com.seai.marine.email_verification.model.VerificationToken;
import com.seai.marine.email_verification.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserAuthenticationMapper userAuthenticationMapper;
    private final EmailVerificationService emailVerificationService;
    private final AuthenticationManager usersAuthenticationProvider;
    private final JwtService jwtService;
    private final TokenGenerator tokenGenerator;
    private final SendVerificationMessageBuilder sendVerificationMessageBuilder;


    public void save(UserRegisterRequest userRegisterRequest, UUID id) {
        UserAuthentication userAuthentication = userAuthenticationMapper.map(userRegisterRequest);
        userAuthenticationRepository.save(userAuthentication, id);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUserId(id);
        verificationToken.setToken(tokenGenerator.generateToken());
        emailVerificationService.saveVerificationToken(verificationToken);
        sendVerificationMessageBuilder.sendVerificationMessage(userRegisterRequest, userAuthentication.getEmail(), verificationToken.getToken());
    }

    public UserAuthenticationResponse authenticateAndGetToken(UserAuthenticationRequest userAuthenticationRequest) {
        Authentication authentication = usersAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(userAuthenticationRequest.getEmail(), userAuthenticationRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            if (emailVerificationService.isEmailConfirmed(securityUser.getId())) {
                return new UserAuthenticationResponse(securityUser.getId().toString(), jwtService.generateToken(userAuthenticationRequest.getEmail()));
            } else {
                throw new ConfirmationException("Email not confirmed!");
            }
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}