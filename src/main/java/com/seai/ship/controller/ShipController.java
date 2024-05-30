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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/ships")
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;

    @GetMapping
    public List<GetShipResponse> getShips(
            @Parameter(description = "Vessel name") @RequestParam(required = false) String vesselName,
            @Parameter(description = "IMO Number") @RequestParam(required = false) Long imoNumber) {
        return shipService.getAll(vesselName, imoNumber);
    }

    @GetMapping("/{shipId}")
    public GetShipByIdResponse getShipById(@PathVariable UUID shipId) {
        return shipService.getShipById(shipId);
    }

    @PutMapping("/{shipId}")
    public void updateShip(@Valid @RequestBody UpdateShipRequest updateShipRequest, @PathVariable UUID shipId) {
        shipService.updateShip(shipId, updateShipRequest);
    }

    @PostMapping
    public CreateShipResponse createShip(@Valid @RequestBody CreateShipRequest createShipRequest) {
        return shipService.createShip(createShipRequest);
    }

    @DeleteMapping("{shipId}")
    public void deleteShip(@PathVariable UUID shipId) {
        shipService.deleteShip(shipId);
    }
}
