package com.seai.manning_agent.sailor.mapper;


import com.seai.manning_agent.sailor.contract.request.CreateSailorRequest;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.UserAuthentication;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SailorMapper {

    User map(CreateSailorRequest createSailorRequest);

    GetUserResponse map(User user);

    UserAuthentication mapToUserAuth(CreateSailorRequest createSailorRequest);
}
