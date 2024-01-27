package com.seai.voyage.mapper;

import com.seai.voyage.model.Voyage;
import com.seai.voyage.contract.request.CreateVoyageRequest;
import com.seai.voyage.contract.request.UpdateVoyageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VoyageMapper {

    Voyage map(CreateVoyageRequest createVoyageRequest);

    Voyage map(UpdateVoyageRequest updateVoyageRequest);
}
