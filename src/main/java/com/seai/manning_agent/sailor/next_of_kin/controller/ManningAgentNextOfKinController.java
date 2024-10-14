package com.seai.manning_agent.sailor.next_of_kin.controller;

import com.seai.manning_agent.sailor.next_of_kin.service.ManningAgentNextOfKinService;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.next_of_kin.contract.request.NextOfKinCreateRequest;
import com.seai.marine.next_of_kin.contract.request.NextOfKinUpdateRequest;
import com.seai.marine.next_of_kin.model.NextOfKin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manning-agents/{manningAgentId}/sailors/{userId}/next-of-kin")
public class ManningAgentNextOfKinController {

    private final ManningAgentNextOfKinService nextOfKinService;

    private static final String AUTHORIZATION = "#manningAgentId.equals(authentication.principal.id)";

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public NextOfKin getNextOfKin (@PathVariable UUID manningAgentId, @PathVariable UUID userId) {
        return nextOfKinService.getNextOfKinByUserId(manningAgentId, userId);
    }

    @PostMapping
    @PreAuthorize(AUTHORIZATION)
    public GetUserResponse createNextOfKin (@PathVariable UUID manningAgentId, @PathVariable UUID userId, @RequestBody NextOfKinCreateRequest nextOfKinCreateRequest) {
        return nextOfKinService.create(manningAgentId ,userId, nextOfKinCreateRequest);
    }

    @PutMapping("/{nextOfKinId}")
    @PreAuthorize(AUTHORIZATION)
    public GetUserResponse updateNextOfKin (@PathVariable UUID manningAgentId, @PathVariable UUID userId, @PathVariable UUID nextOfKinId,
                                      @RequestBody NextOfKinUpdateRequest nextOfKinUpdateRequest) {
        return  nextOfKinService.update(manningAgentId, userId, nextOfKinId ,nextOfKinUpdateRequest);
    }

    @DeleteMapping("/{nextOfKinId}")
    @PreAuthorize(AUTHORIZATION)
    public void deleteNextOfKin (@PathVariable UUID manningAgentId, @PathVariable UUID userId, @PathVariable UUID nextOfKinId) {
        nextOfKinService.delete(manningAgentId, userId, nextOfKinId);
    }
}
