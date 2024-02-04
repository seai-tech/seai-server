package com.seai.marine.user.controller;

import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.contract.request.UserUpdateRequest;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.mapper.UserAuthenticationMapper;
import com.seai.marine.user.mapper.UserMapper;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    private final UserAuthenticationRepository userAuthenticationRepository;

    private final UserAuthenticationMapper userAuthenticationMapper;

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

    @PostMapping("/users")
    public void createUser(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        UUID id = UUID.randomUUID();
        UserAuthentication userAuthentication = userAuthenticationMapper.map(userRegisterRequest);
        userAuthenticationRepository.save(userAuthentication, id);
        User user = userMapper.map(userRegisterRequest);
        userRepository.save(user, id);
    }
}
