package com.seai.manning_agent.sailor.next_of_kin.service;


import com.seai.exception.DuplicatedResourceException;
import com.seai.exception.ResourceNotFoundException;
import com.seai.manning_agent.sailor.mapper.SailorMapper;
import com.seai.manning_agent.sailor.next_of_kin.mapper.ManningAgentNextOfKinMapper;
import com.seai.manning_agent.sailor.next_of_kin.repository.ManningAgentNextOfKinRepository;
import com.seai.manning_agent.sailor.service.ManningAgentSailorService;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.next_of_kin.contract.request.NextOfKinCreateRequest;
import com.seai.marine.next_of_kin.contract.request.NextOfKinUpdateRequest;
import com.seai.marine.next_of_kin.model.NextOfKin;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManningAgentNextOfKinService {

    private final ManningAgentNextOfKinRepository nextOfKinRepository;

    private final ManningAgentSailorService sailorService;

    private final ManningAgentNextOfKinMapper nextOfKinMapper;

    private final SailorMapper sailorMapper;

    public NextOfKin getNextOfKinByUserId(UUID manningAgentId, UUID userId) {
        sailorService.getSailorById(manningAgentId, userId);
        return nextOfKinRepository.findByUserId(userId).orElseThrow(() -> new
                ResourceNotFoundException("NEXT_OF_KIN_FOR_USER_WITH_ID={" + userId + "} not found."));
    }

    @Transactional
    public GetUserResponse create(UUID manningAgentId, UUID userId, NextOfKinCreateRequest nextOfKinRequest) {
        GetUserResponse userResponse = sailorMapper.map(sailorService.getSailorById(manningAgentId, userId).get());

        NextOfKin nextOfKin = nextOfKinMapper.map(nextOfKinRequest);
        nextOfKin.setId(UUID.randomUUID());
        nextOfKin.setUserId(userId);

        userResponse.setNextOfKin(nextOfKin);
        try {
            nextOfKinRepository.save(nextOfKin);
        } catch (DuplicateKeyException e) {
            throw new DuplicatedResourceException("NEXT_OF_KIN_ALREADY_EXISTS_FOR_USER_WITH_ID={" + userId + "}");
        }
        return userResponse;
    }

    @Transactional
    public GetUserResponse update(UUID manningAgentId, UUID userId, UUID nextOfKinId, NextOfKinUpdateRequest nextOfKinUpdateRequest) throws ResourceNotFoundException {
        GetUserResponse userResponse = sailorMapper.map(sailorService.getSailorById(manningAgentId, userId).get());

        NextOfKin nextOfKin = nextOfKinRepository.findById(nextOfKinId).orElseThrow(() -> new
                ResourceNotFoundException("NEXT_OF_KIN_ID={" + nextOfKinId + "} not found."));

        if (!nextOfKin.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("NEXT_OF_KIN_ID={" + nextOfKinId + "} is not associated to " +
                    "USER_ID={" + userId + "}");
        }
        NextOfKin nextOfKinUpdated = nextOfKinMapper.map(nextOfKinUpdateRequest);
        nextOfKinUpdated.setId(nextOfKin.getId());
        nextOfKinUpdated.setUserId(userId);
        nextOfKinRepository.update(nextOfKinUpdated);

        userResponse.setNextOfKin(nextOfKinUpdated);
        return userResponse;
    }

    @Transactional
    public void deleteByUserId(UUID manningAgentId, UUID userId) {
        sailorService.getSailorById(manningAgentId, userId);
        nextOfKinRepository.deleteByUserId(userId);

    }

    @Transactional
    public void delete(UUID manningAgentId, UUID userId, UUID nestOfKinId) {
        sailorService.getSailorById(manningAgentId, userId);
        nextOfKinRepository.delete(userId, nestOfKinId);
    }
}
