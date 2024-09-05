package com.seai.manning_agent.manning_agent.controller;

import com.seai.manning_agent.manning_agent.contract.request.CreateManningAgentRequest;
import com.seai.manning_agent.manning_agent.model.ManningAgent;
import com.seai.manning_agent.manning_agent.service.ManningAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/manning_agents")
public class ManningAgentController {

    private final ManningAgentService manningAgentService;

    @PostMapping
    public ManningAgent createManningAgent(CreateManningAgentRequest createManningAgentRequest) {
        return manningAgentService.createManningAgent(createManningAgentRequest);
    }

    @GetMapping("/{displayId}")
    public Optional<ManningAgent> getManningAgentByDisplayId(String manningAgentId) {
        return manningAgentService.getManningAgentByDisplayId(manningAgentId);
    }
}
