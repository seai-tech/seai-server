package com.seai.user.controller;

import com.seai.user.contract.request.UserUpdateRequest;
import com.seai.user.contract.response.GetUserResponse;
import com.seai.user.model.User;
import com.seai.user.repository.UserRepository;
import com.seai.user.mapper.UserMapper;
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
@RequestMapping("/api/v1")
public class UserController {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @PutMapping("/users/{userId}")
    public void updateUser(@RequestBody @Valid UserUpdateRequest userRegisterRequest, @PathVariable UUID userId) {
        userRepository.findById(userId);
        User user = userMapper.map(userRegisterRequest);
        userRepository.update(userId, user);
    }

    @GetMapping("/users/{userId}")
    public GetUserResponse getUser(@PathVariable UUID userId) {
        User user = userRepository.findById(userId);
        return userMapper.map(user);
    }
}
