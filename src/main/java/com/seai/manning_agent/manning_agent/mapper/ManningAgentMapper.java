package com.seai.manning_agent.manning_agent.mapper;

import com.seai.manning_agent.manning_agent.contract.request.CreateManningAgentRequest;
import com.seai.manning_agent.manning_agent.model.ManningAgent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ManningAgentMapper {

    ManningAgent map(CreateManningAgentRequest createManningAgentRequest);

//    ManningAgent map(ManningAgentUpdateRequest manningAgentUpdateRequest);

}
