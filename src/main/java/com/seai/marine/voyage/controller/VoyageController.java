package com.seai.marine.voyage.controller;

import com.seai.marine.voyage.contract.request.CreateVoyageRequest;
import com.seai.marine.voyage.contract.request.UpdateVoyageRequest;
import com.seai.marine.voyage.contract.response.GetVoyageResponse;
import com.seai.marine.voyage.mapper.VoyageMapper;
import com.seai.marine.voyage.model.Voyage;
import com.seai.marine.voyage.repository.VoyageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class VoyageController {

    private final VoyageMapper voyageMapper;
    private final VoyageRepository voyageRepository;

    @PostMapping("/users/{userId}/voyages")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public Voyage createVoyage(@RequestBody CreateVoyageRequest voyageRequest, @PathVariable UUID userId) {
        Voyage voyage = voyageMapper.map(voyageRequest);
        return voyageRepository.save(voyage, userId);
    }

    @GetMapping("/users/{userId}/voyages")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public List<GetVoyageResponse> findAllByUser(@PathVariable UUID userId) {
        return voyageRepository.findByUserId(userId).stream().map(voyageMapper::map).toList();
    }

    @PutMapping("/users/{userId}/voyages/{voyageId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void updateVoyage(@RequestBody UpdateVoyageRequest updateVoyageRequest, @PathVariable UUID userId, @PathVariable UUID voyageId) {
        Voyage voyage = voyageMapper.map(updateVoyageRequest);
        voyageRepository.update(voyage, userId, voyageId);
    }

    @DeleteMapping("/users/{userId}/voyages/{voyageId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void deleteVoyage(@PathVariable UUID userId, @PathVariable UUID voyageId) {
        voyageRepository.delete(userId, voyageId);
    }
}
