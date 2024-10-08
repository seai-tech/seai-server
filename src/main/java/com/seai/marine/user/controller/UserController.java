package com.seai.marine.user.controller;

import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.contract.request.UserUpdateRequest;
import com.seai.marine.user.contract.response.CreateUserResponse;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.model.Experience;
import com.seai.marine.user.service.UserPhotoService;
import com.seai.marine.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    private static final String AUTHORIZATION = "#userId.equals(authentication.principal.id)";

    @PutMapping("/users/{userId}")
    @PreAuthorize(AUTHORIZATION)
    public void updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest, @PathVariable UUID userId) {
        userService.updateUser(userUpdateRequest, userId);
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize(AUTHORIZATION)
    public void deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize(AUTHORIZATION)
    public GetUserResponse getUser(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/users")
    public CreateUserResponse createUser(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        return userService.createUser(userRegisterRequest);
    }

    @GetMapping("/users/{userId}/experience")
    @PreAuthorize(AUTHORIZATION)
    public List<Experience> getUserExperience(@PathVariable UUID userId) {
        return userService.getUserExperience(userId);
    }

}
