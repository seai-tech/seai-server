package com.seai.ship.controller;

import com.seai.ship.contract.request.CreateShipRequest;
import com.seai.ship.contract.request.UpdateShipRequest;
import com.seai.ship.contract.response.CreateShipResponse;
import com.seai.ship.contract.response.GetShipByIdResponse;
import com.seai.ship.contract.response.GetShipResponse;
import com.seai.ship.service.ShipService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/{userId}/ships")
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;

    @GetMapping
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public List<GetShipResponse> getShips(
            @PathVariable UUID userId,
            @Parameter(description = "Vessel name") @RequestParam(required = false) String vesselName,
            @Parameter(description = "Owner of the ship") @RequestParam(required = false) String owner,
            @Parameter(description = "Ship type") @RequestParam(required = false) String shipType) {
        return shipService.getAll(vesselName, owner, shipType);
    }

    @GetMapping("/{shipId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public GetShipByIdResponse getShipById(@PathVariable UUID userId, @PathVariable UUID shipId) {
        return shipService.getShipById(shipId);
    }

    @PutMapping("/{shipId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void updateShip(@PathVariable UUID userId, @RequestBody @Valid UpdateShipRequest updateShipRequest, @PathVariable UUID shipId) {
        shipService.updateShip(shipId, updateShipRequest);
    }

    @PostMapping
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public CreateShipResponse createShip(@PathVariable UUID userId, @RequestBody @Valid CreateShipRequest createShipRequest) {
        return shipService.createShip(createShipRequest);
    }

    @DeleteMapping("{shipId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void deleteShip(@PathVariable UUID shipId, @PathVariable UUID userId) {
        shipService.deleteShip(shipId);
    }
}
