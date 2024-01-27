package com.seai.user.mapper;

import com.seai.user.contract.request.UserRegisterRequest;
import com.seai.user.model.UserAuthentication;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserAuthenticationMapper {

    UserAuthentication map(UserRegisterRequest userRegisterRequest, UUID id);
}
