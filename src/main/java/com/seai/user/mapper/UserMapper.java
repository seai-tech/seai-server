package com.seai.user.mapper;

import com.seai.user.contract.request.UserRegisterRequest;
import com.seai.user.contract.request.UserUpdateRequest;
import com.seai.user.contract.response.GetUserResponse;
import com.seai.user.model.Sailor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    Sailor map(UserRegisterRequest userRegisterRequest);

    Sailor map(UserUpdateRequest userRegisterRequest);

    GetUserResponse map(Sailor sailor);
}
