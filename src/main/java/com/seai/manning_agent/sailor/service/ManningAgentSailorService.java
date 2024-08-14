package com.seai.manning_agent.sailor.service;

import com.seai.exception.ResourceNotFoundException;
import com.seai.manning_agent.sailor.contract.request.CreateSailorRequest;
import com.seai.manning_agent.sailor.mapper.SailorMapper;
import com.seai.manning_agent.sailor.repository.ManningAgentSailorRepository;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import lombok.RequiredArgsConstructor;
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


    public List<User> getAllSailors(UUID manningAgentId) {
        return manningAgentSailorRepository.getAllSailors(manningAgentId);
    }

    public Optional<User> getSailorById(UUID manningAgentId, UUID sailorId) {
        return Optional.ofNullable(manningAgentSailorRepository.getSailorById(manningAgentId, sailorId).orElseThrow(()
                -> new ResourceNotFoundException("Sailor with id: " + sailorId + " not found.")));
    }

    public User createSailor(UUID manningAgentId, CreateSailorRequest sailorRequest) {
        User sailor = sailorMapper.map(sailorRequest);
        sailor.setId(UUID.randomUUID());
        sailor.setManningAgents(manningAgentId.toString());
        UserAuthentication userAuthentication = sailorMapper.mapToUserAuth(sailorRequest);
        userAuthentication.setPassword(String.valueOf(UUID.randomUUID()));
        userAuthenticationRepository.save(userAuthentication, sailor.getId());
        return manningAgentSailorRepository.createSailor(sailor);
    }

    @Transactional
    public void delete(UUID manningAgentId, UUID sailorId) {
        manningAgentSailorRepository.delete(manningAgentId, sailorId);
        userAuthenticationRepository.delete(sailorId);
    }
}
