package com.seai.manning_agent.sailor.service;

import com.seai.common.display_id_service.DisplayIdService;
import com.seai.common.exception.ResourceNotFoundException;
import com.seai.manning_agent.sailor.contract.request.CreateSailorRequest;
import com.seai.manning_agent.sailor.mapper.SailorMapper;
import com.seai.manning_agent.sailor.repository.ManningAgentSailorRepository;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManningAgentSailorService {

    private final SailorMapper sailorMapper;

    private final ManningAgentSailorRepository manningAgentSailorRepository;

    private final UserAuthenticationRepository userAuthenticationRepository;

    private final DisplayIdService displayIdService;

    @Value("${user.prefix.display-id}")
    private String sailorDisplayIdPrefix;

    public List<User> getAllSailors(UUID manningAgentId) {
        return manningAgentSailorRepository.getAllSailors(manningAgentId);
    }

    public Optional<User> getSailorById(UUID manningAgentId, UUID sailorId) {
        return Optional.ofNullable(manningAgentSailorRepository.getSailorById(manningAgentId, sailorId).orElseThrow(()
                -> new ResourceNotFoundException("SAILOR_ID={" + sailorId + "} not found.")));
    }

    public Optional<User> getSailorByDisplayId(UUID manningAgentId, String sailorId) {
        return Optional.ofNullable(manningAgentSailorRepository.findSailorByDisplayId(manningAgentId, sailorId).orElseThrow(()
                -> new ResourceNotFoundException("SAILOR_DISPLAY_ID={" + sailorId + "} not found.")));
    }

    @Transactional
    public User createSailor(UUID manningAgentId, CreateSailorRequest sailorRequest) {
        UUID sailorId = UUID.randomUUID();
        UserAuthentication userAuthentication = sailorMapper.mapToUserAuth(sailorRequest);
        userAuthentication.setPassword(String.valueOf(UUID.randomUUID()));
        User sailor = sailorMapper.map(sailorRequest);
        sailor.setId(sailorId);
        sailor.setManningAgents(manningAgentId.toString());
        sailor.setDisplayId(displayIdService.generateDisplayId(sailorDisplayIdPrefix, manningAgentId));
        userAuthenticationRepository.save(userAuthentication, sailorId);
        return manningAgentSailorRepository.createSailor(sailor);
    }

    @Transactional
    public void delete(UUID manningAgentId, UUID sailorId) {
        manningAgentSailorRepository.delete(manningAgentId, sailorId);
        userAuthenticationRepository.delete(sailorId);
    }
}
