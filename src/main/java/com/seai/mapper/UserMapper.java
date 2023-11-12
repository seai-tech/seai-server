package com.seai.mapper;

import com.seai.domain.user.model.SeaiUser;
import com.seai.request.UserRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    SeaiUser map(UserRegisterRequest userRegisterRequest);
}
