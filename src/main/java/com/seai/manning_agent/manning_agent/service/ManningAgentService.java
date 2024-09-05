package com.seai.manning_agent.manning_agent.service;

import com.seai.common.display_id_service.DisplayIdService;
import com.seai.common.exception.ResourceNotFoundException;
import com.seai.manning_agent.manning_agent.contract.request.CreateManningAgentRequest;
import com.seai.manning_agent.manning_agent.mapper.ManningAgentAuthMapper;
import com.seai.manning_agent.manning_agent.mapper.ManningAgentMapper;
import com.seai.manning_agent.manning_agent.model.ManningAgent;
import com.seai.manning_agent.manning_agent.model.ManningAgentAuthentication;
import com.seai.manning_agent.manning_agent.repository.ManningAgentRepository;
import com.seai.manning_agent.manning_agent.repository.ManningAgentAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManningAgentService {

    private final ManningAgentRepository manningAgentRepository;

    private final ManningAgentAuthRepository manningAgentAuthRepository;

    private final ManningAgentMapper manningAgentMapper;

    private final ManningAgentAuthMapper manningAgentAuthMapper;

    private final DisplayIdService displayIdService;

    @Value("${manning-agent.prefix.display-id}")
    private String displayIdPrefix;

    public ManningAgent createManningAgent(CreateManningAgentRequest createManningAgentRequest) {
        ManningAgent manningAgent = manningAgentMapper.map(createManningAgentRequest);
        manningAgent.setId(UUID.randomUUID());
        ManningAgentAuthentication manningAgentAuthentication = manningAgentAuthMapper.map(createManningAgentRequest);
        manningAgentAuthentication.setId(manningAgent.getId());
        manningAgent.setDisplayId(displayIdService.generateDisplayId(displayIdPrefix, null));
        manningAgentAuthRepository.save(manningAgentAuthentication);
        manningAgentRepository.save(manningAgent);
        return manningAgent;
    }

    public Optional<ManningAgent> getManningAgentByDisplayId(String displayId) {
        return Optional.ofNullable(manningAgentRepository.findByDisplayId(displayId).orElseThrow(() ->
                new ResourceNotFoundException("MANNING_AGENT_DISPLAY_ID={" + displayId + "} not found")));
    }
}
