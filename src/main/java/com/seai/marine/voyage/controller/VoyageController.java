package com.seai.marine.voyage.controller;

import com.seai.marine.voyage.contract.request.CreateVoyageRequest;
import com.seai.marine.voyage.contract.request.UpdateVoyageRequest;
import com.seai.marine.voyage.contract.response.GetVoyageResponse;
import com.seai.marine.voyage.model.Voyage;
import com.seai.marine.voyage.service.VoyageService;
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
@RequestMapping("api/v1/users/{userId}/voyages")
@RequiredArgsConstructor
public class VoyageController {

    private final VoyageService voyageService;

    private static final String AUTHORIZATION = "#userId.equals(authentication.principal.id)";

    @PostMapping
    @PreAuthorize(AUTHORIZATION)
    public Voyage createVoyage(@RequestBody CreateVoyageRequest voyageRequest, @PathVariable UUID userId) {
        return voyageService.createVoyage(voyageRequest, userId);
    }

    @GetMapping("/all")
    @PreAuthorize(AUTHORIZATION)
    public List<GetVoyageResponse> getAllForUser(@PathVariable UUID userId) {
        return voyageService.getAllForUser(userId);
    }

    @GetMapping("/{voyageId}")
    @PreAuthorize(AUTHORIZATION)
    public GetVoyageResponse getVoyageById(@PathVariable UUID userId, @PathVariable UUID voyageId) {
        return voyageService.getById(userId, voyageId);
    }

    @PutMapping("/{voyageId}")
    @PreAuthorize(AUTHORIZATION)
    public Voyage updateVoyage(@RequestBody UpdateVoyageRequest updateVoyageRequest, @PathVariable UUID userId, @PathVariable UUID voyageId) {
        return voyageService.update(updateVoyageRequest, userId, voyageId);
    }

    @DeleteMapping("/{voyageId}")
    @PreAuthorize(AUTHORIZATION)
    public void deleteVoyage(@PathVariable UUID userId, @PathVariable UUID voyageId) {
        voyageService.delete(userId, voyageId);
    }
}
