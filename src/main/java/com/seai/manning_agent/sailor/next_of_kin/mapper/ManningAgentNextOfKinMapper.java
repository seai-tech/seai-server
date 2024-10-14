package com.seai.manning_agent.sailor.next_of_kin.mapper;

import com.seai.marine.next_of_kin.contract.request.NextOfKinCreateRequest;
import com.seai.marine.next_of_kin.contract.request.NextOfKinUpdateRequest;
import com.seai.marine.next_of_kin.model.NextOfKin;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ManningAgentNextOfKinMapper {

    NextOfKin map(NextOfKinCreateRequest nextOfKinCreateRequest);

    NextOfKin map(NextOfKinUpdateRequest nextOfKinUpdateRequest);

}
