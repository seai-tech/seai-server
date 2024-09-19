package com.seai.training_center.online_course.controller;

import com.seai.training_center.online_course.service.OnlineCourseFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/training-centers")
public class OnlineCourseFileController {

    private final OnlineCourseFileService onlineCourseFileService;

    @PostMapping("/{trainingCenterId}/online-courses/{courseId}/files")
    public void upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        onlineCourseFileService.upload(multipartFile, trainingCenterId, courseId);
    }

    @DeleteMapping("/{trainingCenterId}/online-courses/{courseId}/files")
    public void delete(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        onlineCourseFileService.delete(trainingCenterId, courseId);
    }

    @GetMapping("/{trainingCenterId}/online-courses/{courseId}/files")
    public ResponseEntity<byte[]> download(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        byte[] bytes = onlineCourseFileService.download(trainingCenterId, courseId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", "file");
        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.DAYS)
                .noTransform()
                .mustRevalidate();
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .headers(httpHeaders)
                .body(bytes);
    }
}
