package com.seai.marine.next_of_kin.controller;

import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.next_of_kin.contract.request.NextOfKinCreateRequest;
import com.seai.marine.next_of_kin.contract.request.NextOfKinUpdateRequest;
import com.seai.marine.next_of_kin.model.NextOfKin;
import com.seai.marine.next_of_kin.service.NextOfKinService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/next-of-kin")
public class NextOfKinController {

    private final NextOfKinService nextOfKinService;

    private static final String AUTHORIZATION = "#userId.equals(authentication.principal.id)";

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public NextOfKin getNextOfKin (@PathVariable UUID userId) {
        return nextOfKinService.getNextOfKinByUserId(userId);
    }

    @PostMapping
    @PreAuthorize(AUTHORIZATION)
    public GetUserResponse createNextOfKin (@PathVariable UUID userId, @RequestBody NextOfKinCreateRequest nextOfKinCreateRequest) {
        return nextOfKinService.create(userId, nextOfKinCreateRequest);
    }

    @PutMapping("/{nextOfKinId}")
    @PreAuthorize(AUTHORIZATION)
    public NextOfKin updateNextOfKin (@PathVariable UUID userId, @PathVariable UUID nextOfKinId,
                                      @RequestBody NextOfKinUpdateRequest nextOfKinUpdateRequest) {
        return  nextOfKinService.update(userId, nextOfKinId ,nextOfKinUpdateRequest);
    }

    @DeleteMapping("/{nextOfKinId}")
    @PreAuthorize(AUTHORIZATION)
    public void deleteNextOfKin (@PathVariable UUID userId, @PathVariable UUID nextOfKinId) {
        nextOfKinService.delete(userId, nextOfKinId);
    }
}
