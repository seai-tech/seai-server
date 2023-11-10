package com.seai.mapper;

import com.seai.domain.voyage.model.Voyage;
import com.seai.request.CreateVoyageRequest;
import com.seai.request.UpdateVoyageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VoyageMapper {

    Voyage map(CreateVoyageRequest createVoyageRequest);

    Voyage map(UpdateVoyageRequest updateVoyageRequest);
}
