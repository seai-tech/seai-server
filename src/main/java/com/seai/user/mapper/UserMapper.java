package com.seai.user.mapper;

import com.seai.user.contract.request.UserRegisterRequest;
import com.seai.user.contract.request.UserUpdateRequest;
import com.seai.user.contract.response.GetUserResponse;
import com.seai.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User map(UserRegisterRequest userRegisterRequest);

    User map(UserUpdateRequest userRegisterRequest);

    GetUserResponse map(User user);
}
