package com.seai.manning_agent.voyage.controller;

import com.seai.marine.voyage.contract.request.CreateVoyageRequest;
import com.seai.marine.voyage.contract.request.UpdateVoyageRequest;
import com.seai.marine.voyage.contract.response.GetVoyageResponse;
import com.seai.manning_agent.voyage.service.ManningAgentVoyageService;
import com.seai.marine.voyage.model.Voyage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/manning-agents")
@RequiredArgsConstructor
public class ManningAgentVoyageController {
    private final ManningAgentVoyageService manningAgentVoyageService;
    private static final String AUTHORIZATION = "#manningAgentId.equals(authentication.principal.id)";


    @PostMapping("{manningAgentId}/sailors/{sailorId}/voyages")
    @PreAuthorize(AUTHORIZATION)
    public Voyage createSailorVoyage(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @RequestBody CreateVoyageRequest voyageRequest) {
        return manningAgentVoyageService.createSailorVoyage(manningAgentId, sailorId, voyageRequest);
    }

    @GetMapping("{manningAgentId}/sailors/{sailorId}/voyages")
    @PreAuthorize(AUTHORIZATION)
    public List<GetVoyageResponse> findAllByUser(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId) {
        return manningAgentVoyageService.findAllByUser(manningAgentId, sailorId);
    }

    @PutMapping("{manningAgentId}/sailors/{sailorId}/voyages/{voyageId}")
    @PreAuthorize(AUTHORIZATION)
    public void updateVoyage(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @PathVariable UUID voyageId, @RequestBody UpdateVoyageRequest updateVoyageRequest) {
        manningAgentVoyageService.updateVoyage(updateVoyageRequest, manningAgentId, sailorId, voyageId);
    }

    @DeleteMapping("{manningAgentId}/sailors/{sailorId}/voyages/{voyageId}")
    @PreAuthorize(AUTHORIZATION)
    public void deleteVoyage(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @PathVariable UUID voyageId) {
        manningAgentVoyageService.deleteVoyage(manningAgentId, sailorId, voyageId);
    }
}
