package com.seai.marine.user.service;

import com.seai.exception.ConfirmationException;
import com.seai.marine.notification.email.EmailSender;
import com.seai.marine.user.contract.request.UserAuthenticationRequest;
import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.contract.response.UserAuthenticationResponse;
import com.seai.marine.user.mapper.UserAuthenticationMapper;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.spring.security.model.SecurityUser;
import com.seai.spring.security.service.JwtService;
import com.seai.spring.security.token.model.ConfirmationToken;
import com.seai.spring.security.token.service.ConfirmationTokenService;
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
    private final ConfirmationTokenService confirmationTokenService;
    private final AuthenticationManager usersAuthenticationProvider;
    private final EmailSender emailSender;
    private final JwtService jwtService;


    public void save(UserRegisterRequest userRegisterRequest, UUID id) {
        UserAuthentication userAuthentication = userAuthenticationMapper.map(userRegisterRequest);
        userAuthenticationRepository.save(userAuthentication, id);
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUserId(id);
        confirmationToken.setToken(jwtService.generateToken(userAuthentication.getEmail()));
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String message = getMessage(userRegisterRequest, confirmationToken);
        emailSender.sendSimpleMessage(userRegisterRequest.getEmail(), "Email Confirmation", message);
    }

    public UserAuthenticationResponse authenticateAndGetToken(UserAuthenticationRequest userAuthenticationRequest) {
        Authentication authentication = usersAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(userAuthenticationRequest.getEmail(), userAuthenticationRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            if (confirmationTokenService.isEmailConfirmed(securityUser.getId())) {
                return new UserAuthenticationResponse(securityUser.getId().toString(), jwtService.generateToken(userAuthenticationRequest.getEmail()));
            } else {
                throw new ConfirmationException("Email not confirmed!");
            }
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    private static String getMessage(UserRegisterRequest userRegisterRequest, ConfirmationToken confirmationToken) {
        String confirmationLink = "http://localhost:8080/api/v1/users/confirm?token=" + confirmationToken.getToken();
        String message = String.format(
                "Dear %s,\n\n" +
                        "Thank you for registering with SeAI. To complete your registration, please confirm your email address by clicking the button below:\n\n" +
                        "%s\n\n" +
                        "Confirmation token have 15 minutes expiration time\n"+
                        "If you did not create an account with us, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "The SeAI.co Team",
                userRegisterRequest.getFirstName(),
                confirmationLink
        );
        return message;
    }
}