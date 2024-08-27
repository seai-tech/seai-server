package com.seai.manning_agent.sailor.controller;


import com.seai.manning_agent.sailor.contract.request.CreateSailorRequest;
import com.seai.manning_agent.sailor.service.ManningAgentSailorService;
import com.seai.marine.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/manning-agents/{manningAgentId}/sailors")
public class ManningAgentSailorController {

    private final ManningAgentSailorService manningAgentSailorService;
    private static final String AUTHORIZATION = "#manningAgentId.equals(authentication.principal.id)";

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public List<User> getAllSailors(@PathVariable UUID manningAgentId) {
        return manningAgentSailorService.getAllSailors(manningAgentId);
    }

    @GetMapping("/{sailorId}")
    @PreAuthorize(AUTHORIZATION)
    public Optional<User> getSailorById(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId) {
        return manningAgentSailorService.getSailorById(manningAgentId, sailorId);
    }

    @PostMapping("/sailors")
    @PreAuthorize(AUTHORIZATION)
    public User createSailor(@PathVariable UUID manningAgentId, @RequestBody @Valid CreateSailorRequest createSailorRequest) {
        return manningAgentSailorService.createSailor(manningAgentId, createSailorRequest);
    }

    @DeleteMapping("/{sailorId}")
    @PreAuthorize(AUTHORIZATION)
    public void deleteUser(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId) {
        manningAgentSailorService.delete(manningAgentId, sailorId);
    }
}
