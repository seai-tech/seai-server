package com.seai.manning_agent.manning_agent.service;

import com.seai.manning_agent.manning_agent.contract.request.ManningAgentAuthenticationRequest;
import com.seai.manning_agent.manning_agent.contract.response.ManningAgentAuthenticationResponse;
import com.seai.spring.security.model.SecurityUser;
import com.seai.spring.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ManningAgentAuthService {

    private final JwtService jwtService;
    private final AuthenticationProvider manningAgentAuthProvider;

    @Autowired
    public ManningAgentAuthService(JwtService jwtService, @Qualifier("manningAgentAuthenticationProvider")
    AuthenticationProvider manningAgentAuthProvider) {
        this.jwtService = jwtService;
        this.manningAgentAuthProvider = manningAgentAuthProvider;
    }


    public ManningAgentAuthenticationResponse authenticateAndGetToken(ManningAgentAuthenticationRequest manningAgentAuthenticationRequest) {
        Authentication authentication = manningAgentAuthProvider.authenticate(new UsernamePasswordAuthenticationToken(manningAgentAuthenticationRequest.getEmail(), manningAgentAuthenticationRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new ManningAgentAuthenticationResponse(((SecurityUser) authentication.getPrincipal()).getId().toString(), jwtService.generateToken(manningAgentAuthenticationRequest.getEmail()));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

}
