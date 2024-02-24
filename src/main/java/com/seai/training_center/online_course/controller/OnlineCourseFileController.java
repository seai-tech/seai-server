package com.seai.training_center.online_course.controller;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.seai.training_center.online_course.model.OnlineCourse;
import com.seai.training_center.online_course.repository.OnlineCourseRepository;
import com.seai.training_center.online_course.service.OnlineCourseFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/training-centers")
public class OnlineCourseFileController {

    private final OnlineCourseRepository onlineCourseRepository;
    private final OnlineCourseFileService onlineCourseFileService;

    @PostMapping("/{trainingCenterId}/online-courses/{courseId}/files")
    public void upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        OnlineCourse onlineCourse = onlineCourseRepository.find(trainingCenterId, courseId);
        onlineCourseFileService.upload(multipartFile, onlineCourse.getPath());
    }

    @DeleteMapping("/{trainingCenterId}/online-courses/{courseId}/files")
    public void delete(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        OnlineCourse onlineCourse = onlineCourseRepository.find(trainingCenterId, courseId);
        onlineCourseFileService.delete(onlineCourse.getPath());
    }

    @GetMapping("/{trainingCenterId}/online-courses/{courseId}/files")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable UUID trainingCenterId, @PathVariable UUID courseId) {
        OnlineCourse onlineCourse = onlineCourseRepository.find(trainingCenterId, courseId);
        try (S3ObjectInputStream video = onlineCourseFileService.download(onlineCourse)) {
            final StreamingResponseBody body = outputStream -> {
                int numberOfBytesToWrite = 0;
                byte[] data = new byte[1024];
                while ((numberOfBytesToWrite = video.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, numberOfBytesToWrite);
                }
            };
            return ResponseEntity.ok()
                    .body(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
