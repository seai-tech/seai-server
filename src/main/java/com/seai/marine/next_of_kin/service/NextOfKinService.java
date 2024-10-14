package com.seai.marine.next_of_kin.service;


import com.seai.exception.DuplicatedResourceException;
import com.seai.exception.ResourceNotFoundException;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.service.UserService;
import com.seai.marine.next_of_kin.contract.request.NextOfKinCreateRequest;
import com.seai.marine.next_of_kin.contract.request.NextOfKinUpdateRequest;
import com.seai.marine.next_of_kin.mapper.NextOfKinMapper;
import com.seai.marine.next_of_kin.repository.NextOfKinRepository;
import com.seai.marine.next_of_kin.model.NextOfKin;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NextOfKinService {

    private final NextOfKinRepository nextOfKinRepository;

    private final NextOfKinMapper nextOfKinMapper;

    private final UserService userService;


    public NextOfKin getNextOfKinByUserId(UUID userId) {
        return nextOfKinRepository.findByUserId(userId).orElseThrow(() -> new
                ResourceNotFoundException("NEXT_OF_KIN_FOR_USER_WITH_ID={" + userId + "} not found."));
    }

    public NextOfKin getNextOfKinById(UUID nextOfKinId) {
        return nextOfKinRepository.findById(nextOfKinId).orElseThrow(() -> new
                ResourceNotFoundException("NEXT_OF_KIN_ID={" + nextOfKinId + "} not found."));
    }

    @Transactional
    public GetUserResponse create(UUID userId, NextOfKinCreateRequest nextOfKinRequest) {
        GetUserResponse userResponse = userService.getUserById(userId);
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
    public NextOfKin update(UUID userId, UUID nextOfKinId, NextOfKinUpdateRequest nextOfKinUpdateRequest) throws ResourceNotFoundException {
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
        return nextOfKinUpdated;
    }

    @Transactional
    public void delete(UUID userId, UUID nestOfKinId) {
        nextOfKinRepository.delete(userId, nestOfKinId);
    }
}
