package com.seai.manning_agent.manning_agent.controller;


import com.seai.manning_agent.manning_agent.contract.request.ManningAgentAuthenticationRequest;
import com.seai.manning_agent.manning_agent.contract.response.ManningAgentAuthenticationResponse;
import com.seai.manning_agent.manning_agent.service.ManningAgentAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/manning-agents")
public class ManningAgentAuthController {

    private final ManningAgentAuthService manningAgentAuthService;

    @PostMapping("/authentication")
    public ManningAgentAuthenticationResponse authenticateAndGetToken(@RequestBody @Valid ManningAgentAuthenticationRequest manningAgentAuthenticationRequest) {
        return manningAgentAuthService.authenticateAndGetToken(manningAgentAuthenticationRequest);
    }
}
