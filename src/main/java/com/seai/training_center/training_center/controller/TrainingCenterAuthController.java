package com.seai.training_center.training_center.controller;


import com.seai.spring.security.model.SecurityUser;
import com.seai.spring.security.service.JwtService;
import com.seai.training_center.training_center.contract.request.TrainingCenterAuthenticationRequest;
import com.seai.training_center.training_center.contract.response.TrainingCenterAuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/training-centers")
public class TrainingCenterAuthController {

    private final JwtService jwtService;

    private final AuthenticationProvider trainingCentersAuthenticationProvider;

    @PostMapping("/authentication")
    public TrainingCenterAuthenticationResponse authenticateAndGetToken(@RequestBody @Valid TrainingCenterAuthenticationRequest trainingCenterAuthenticationRequest) {
        Authentication authentication = trainingCentersAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(trainingCenterAuthenticationRequest.getEmail(), trainingCenterAuthenticationRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new TrainingCenterAuthenticationResponse(((SecurityUser)authentication.getPrincipal()).getId().toString(), jwtService.generateToken(trainingCenterAuthenticationRequest.getEmail()));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
