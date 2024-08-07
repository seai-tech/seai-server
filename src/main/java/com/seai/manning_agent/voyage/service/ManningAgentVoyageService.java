package com.seai.manning_agent.voyage.service;

import com.seai.manning_agent.sailor.service.ManningAgentSailorService;
import com.seai.marine.voyage.contract.request.CreateVoyageRequest;
import com.seai.marine.voyage.contract.request.UpdateVoyageRequest;
import com.seai.marine.voyage.contract.response.GetVoyageResponse;
import com.seai.marine.voyage.mapper.VoyageMapper;
import com.seai.marine.voyage.model.Voyage;
import com.seai.marine.voyage.repository.VoyageRepository;
import com.seai.marine.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManningAgentVoyageService {

    private final VoyageMapper voyageMapper;

    private final ManningAgentSailorService manningAgentSailorService;

    private final VoyageRepository voyageRepository;

    public Voyage createSailorVoyage(UUID manningAgentId ,UUID sailorId, CreateVoyageRequest voyageRequest) {
        Optional<User> user = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        Voyage voyage = voyageMapper.map(voyageRequest);
        voyage.setId(UUID.randomUUID());
        return voyageRepository.save(voyage, user.get().getId());
    }

    public List<GetVoyageResponse> findAllByUser(UUID manningAgentId, UUID sailorId) {
        Optional<User> user = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        return voyageRepository.findByUserId(user.get().getId()).stream().map(voyageMapper::map).toList();
    }

    public void updateVoyage(UpdateVoyageRequest updateVoyageRequest, UUID manningAgentId, UUID sailorId, UUID voyageId) {
        Optional<User> user = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        Voyage voyage = voyageMapper.map(updateVoyageRequest);
        voyageRepository.update(voyage, user.get().getId(), voyageId);
    }


    public void deleteVoyage(UUID manningAgentId, UUID sailorId, UUID voyageId) {
        Optional<User> user = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        if (user.isPresent()) {
            voyageRepository.delete(sailorId, voyageId);
        }
    }
}
