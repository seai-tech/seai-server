package com.seai.marine.user.mapper;

import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.model.UserAuthentication;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserAuthenticationMapper {

    UserAuthentication map(UserRegisterRequest userRegisterRequest);
}
