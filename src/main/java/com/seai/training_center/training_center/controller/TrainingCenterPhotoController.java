package com.seai.training_center.training_center.controller;

import com.seai.training_center.training_center.service.TrainingCenterPhotoUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/training-centers/{trainingCenterId}/photo")
public class TrainingCenterPhotoController {

    private final TrainingCenterPhotoUploadService photoUploadService;

    private static final String AUTHORIZATION = "#trainingCenterId.equals(authentication.principal.id)";

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize(AUTHORIZATION)
    public void updatePhoto(@PathVariable UUID trainingCenterId, @RequestParam("file") MultipartFile multipartFile) {
        photoUploadService.updatePhoto(trainingCenterId, multipartFile);
    }

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public ResponseEntity<byte[]> download(@PathVariable UUID trainingCenterId) {
        return photoUploadService.downloadPhoto(trainingCenterId);
    }

    @DeleteMapping
    @PreAuthorize(AUTHORIZATION)
    public void deletePhoto(@PathVariable UUID trainingCenterId) {
        photoUploadService.deletePhoto(trainingCenterId);
    }
}
