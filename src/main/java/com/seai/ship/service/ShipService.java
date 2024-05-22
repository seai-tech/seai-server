package com.seai.ship.service;

import com.seai.ship.contract.request.CreateShipRequest;
import com.seai.ship.contract.request.UpdateShipRequest;
import com.seai.ship.contract.response.CreateShipResponse;
import com.seai.ship.contract.response.GetShipByIdResponse;
import com.seai.ship.contract.response.GetShipResponse;
import com.seai.ship.mapper.ShipMapper;
import com.seai.ship.model.Ship;
import com.seai.ship.repository.ShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ShipService {
    private final ShipRepository shipRepository;
    private final ShipMapper shipMapper;

    public List<GetShipResponse> getAll(String vesselName, String owner, String shipType) {
        return shipRepository.findAll(vesselName, owner, shipType).stream()
                .map(shipMapper::mapToShip).toList();
    }

    public GetShipByIdResponse getShipById(UUID id) {
        Ship ship = shipRepository.findById(id);
        return shipMapper.mapToGetShipByIdResponse(ship);
    }

    public CreateShipResponse createShip(CreateShipRequest createShipRequest) {
        Ship ship = shipMapper.mapToShip(createShipRequest);
        shipRepository.save(ship);
        return shipMapper.mapToCreateShipResponse(ship);
    }

    public void updateShip(UUID shipId, UpdateShipRequest updateShipRequest) {
        getShipById(shipId);
        Ship ship = shipMapper.mapToShip(updateShipRequest);
        shipRepository.update(ship);
    }

    public void deleteShip(UUID id) {
        shipRepository.delete(id);
    }
}
