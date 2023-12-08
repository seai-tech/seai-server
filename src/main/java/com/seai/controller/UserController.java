package com.seai.controller;

import com.seai.domain.user.model.SeaiUser;
import com.seai.domain.user.repository.UserRepository;
import com.seai.mapper.UserMapper;
import com.seai.request.UserUpdateRequest;
import com.seai.response.GetUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody @Valid UserUpdateRequest userRegisterRequest, @PathVariable UUID userId) {
        userRepository.findById(userId);
        SeaiUser user = userMapper.map(userRegisterRequest);
        userRepository.update(userId, user);
    }

    @GetMapping("/{userId}")
    public GetUserResponse getUser(@PathVariable UUID userId) {
        SeaiUser seaiUser = userRepository.findById(userId);
        return userMapper.map(seaiUser);
    }
}
