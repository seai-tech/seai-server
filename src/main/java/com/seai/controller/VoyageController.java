package com.seai.controller;

import com.seai.domain.voyage.model.Voyage;
import com.seai.domain.voyage.repository.VoyageRepository;
import com.seai.mapper.VoyageMapper;
import com.seai.request.CreateVoyageRequest;
import com.seai.request.UpdateVoyageRequest;
import lombok.RequiredArgsConstructor;
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
    public void createVoyage(@RequestBody CreateVoyageRequest voyageRequest, @PathVariable UUID userId) {
        Voyage voyage = voyageMapper.map(voyageRequest);
        voyageRepository.save(voyage, userId);
    }

    @PutMapping("/users/{userId}/voyages/{voyageId}")
    public void updateVoyage(@RequestBody UpdateVoyageRequest updateVoyageRequest, @PathVariable UUID userId, @PathVariable UUID voyageId) {
        Voyage voyage = voyageMapper.map(updateVoyageRequest);
        voyageRepository.update(voyage, userId, voyageId);
    }

    @GetMapping("/users/{userId}/voyages")
    public List<Voyage> findAllByUser(@PathVariable UUID userId) {
        return voyageRepository.findByUserId(userId);
    }

    @DeleteMapping("/users/{userId}/voyages/{voyageId}")
    public void deleteVoyage(@PathVariable UUID userId, @PathVariable UUID voyageId) {
        voyageRepository.delete(userId, voyageId);
    }
}
