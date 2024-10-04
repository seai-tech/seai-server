package com.seai.marine.voyage.service;

import com.seai.exception.ResourceNotFoundException;
import com.seai.marine.voyage.contract.request.CreateVoyageRequest;
import com.seai.marine.voyage.contract.request.UpdateVoyageRequest;
import com.seai.marine.voyage.contract.response.GetVoyageResponse;
import com.seai.marine.voyage.mapper.VoyageMapper;
import com.seai.marine.voyage.model.Voyage;
import com.seai.marine.voyage.repository.VoyageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoyageService {

    private final VoyageRepository voyageRepository;

    private final VoyageMapper voyageMapper;


    @Transactional
    public Voyage createVoyage(CreateVoyageRequest createVoyageRequest, UUID userId) {
        Voyage voyage = voyageMapper.map(createVoyageRequest);
        return voyageRepository.save(voyage, userId);
    }

    public List<GetVoyageResponse> getAllForUser(UUID userId) {
        return voyageRepository.findByUserId(userId)
                .stream()
                .map(voyageMapper::map)
                .toList();
    }

    public GetVoyageResponse getById(UUID userId, UUID voyageId) {
        Voyage voyage = voyageRepository.findById(userId, voyageId)
                .orElseThrow(() -> new ResourceNotFoundException("VOYAGE_ID={"+voyageId+"} NOT FOUND."));
        return voyageMapper.map(voyage);
    }

    @Transactional
    public Voyage update(UpdateVoyageRequest updateVoyageRequest, UUID userId, UUID voyageId) {
        Voyage voyage = voyageMapper.map(updateVoyageRequest);
        return voyageRepository.update(voyage, userId, voyageId);
    }

    @Transactional
    public void delete(UUID userId, UUID voyageId) {
        voyageRepository.delete(userId, voyageId);
    }

}
