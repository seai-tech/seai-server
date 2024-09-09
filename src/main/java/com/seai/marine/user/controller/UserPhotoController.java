package com.seai.marine.user.controller;

import com.seai.marine.user.service.UserPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/{userId}/photo")
public class UserPhotoController {

    private final UserPhotoService userPhotoService;

    private static final String AUTHORIZATION = "#userId.equals(authentication.principal.id)";

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize(AUTHORIZATION)
    public void updatePhoto(@PathVariable UUID userId, @RequestParam("file") MultipartFile multipartFile) {
        userPhotoService.updatePhoto(userId, multipartFile);
    }

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public ResponseEntity<byte[]> download(@PathVariable UUID userId) {
        return userPhotoService.downloadPhoto(userId);
    }

    @DeleteMapping
    @PreAuthorize(AUTHORIZATION)
    public void deletePhoto(@PathVariable UUID userId) {
        userPhotoService.deletePhoto(userId);
    }

}
