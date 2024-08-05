package com.seai.manning_agent.manning_agent.service;

import com.seai.manning_agent.manning_agent.contract.request.CreateManningAgentRequest;
import com.seai.manning_agent.manning_agent.mapper.ManningAgentAuthMapper;
import com.seai.manning_agent.manning_agent.mapper.ManningAgentMapper;
import com.seai.manning_agent.manning_agent.model.ManningAgent;
import com.seai.manning_agent.manning_agent.model.ManningAgentAuthentication;
import com.seai.manning_agent.manning_agent.repository.ManningAgentRepository;
import com.seai.manning_agent.manning_agent.repository.ManningAgentAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManningAgentService {

    private final ManningAgentRepository manningAgentRepository;

    private final ManningAgentAuthRepository manningAgentAuthRepository;

    private final ManningAgentMapper manningAgentMapper;

    private final ManningAgentAuthMapper manningAgentAuthMapper;


    public ManningAgent createManningAgent(CreateManningAgentRequest createManningAgentRequest) {
        ManningAgent manningAgent = manningAgentMapper.map(createManningAgentRequest);
        manningAgent.setId(UUID.randomUUID());
        ManningAgentAuthentication manningAgentAuthentication = manningAgentAuthMapper.map(createManningAgentRequest);
        manningAgentAuthentication.setId(manningAgent.getId());
        manningAgentAuthRepository.save(manningAgentAuthentication);
        manningAgentRepository.save(manningAgent);
        return manningAgent;
    }

}
