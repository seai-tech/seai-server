package com.seai.marine.voyage.mapper;

import com.seai.marine.voyage.model.Voyage;
import com.seai.marine.voyage.contract.request.CreateVoyageRequest;
import com.seai.marine.voyage.contract.request.UpdateVoyageRequest;
import com.seai.marine.voyage.contract.response.GetVoyageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VoyageMapper {

    Voyage map(CreateVoyageRequest createVoyageRequest);

    Voyage map(UpdateVoyageRequest updateVoyageRequest);

    GetVoyageResponse map(Voyage updateVoyageRequest);
}
