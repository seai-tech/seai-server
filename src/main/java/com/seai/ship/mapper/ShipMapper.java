package com.seai.ship.mapper;

import com.seai.ship.contract.request.CreateShipRequest;
import com.seai.ship.contract.request.UpdateShipRequest;
import com.seai.ship.contract.response.CreateShipResponse;
import com.seai.ship.contract.response.GetShipByIdResponse;
import com.seai.ship.contract.response.GetShipResponse;
import com.seai.ship.model.Ship;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShipMapper {
    GetShipResponse mapToShip(Ship ship);

    GetShipByIdResponse mapToGetShipByIdResponse(Ship ship);

    CreateShipResponse mapToCreateShipResponse(Ship ship);

    Ship mapToShip(CreateShipRequest createShipRequest);

    Ship mapToShip(UpdateShipRequest updateShipRequest);
}
