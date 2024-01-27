package com.seai.voyage.mapper;

import com.seai.user.model.SeaiUser;
import com.seai.user.contract.request.UserUpdateRequest;
import com.seai.user.contract.request.UserRegisterRequest;
import com.seai.user.contract.response.GetUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    SeaiUser map(UserRegisterRequest userRegisterRequest);

    SeaiUser map(UserUpdateRequest userRegisterRequest);

    GetUserResponse map(SeaiUser seaiUser);
}
