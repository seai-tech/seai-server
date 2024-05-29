package com.seai.marine.user.mapper;

import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.contract.request.UserUpdateRequest;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User map(UserRegisterRequest userRegisterRequest);

    User map(UserUpdateRequest userRegisterRequest);

    GetUserResponse mapToGetUserResponse(User user);
}
