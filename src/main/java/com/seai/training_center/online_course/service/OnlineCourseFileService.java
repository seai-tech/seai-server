package com.seai.training_center.online_course.service;

import com.seai.exception.ResourceNotFoundException;
import com.seai.training_center.online_course.model.OnlineCourse;
import com.seai.training_center.online_course.repository.OnlineCourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnlineCourseFileService {

    private static final String BUCKET = "training-center-online-courses";

    private final S3Client s3client;

    private final OnlineCourseService onlineCourseService;


    @SneakyThrows
    public void upload(MultipartFile multipartFile, UUID trainingCenterId, UUID courseId) {
        OnlineCourse onlineCourse = onlineCourseService.getTrainingCenterCourseById(trainingCenterId, courseId);

        RequestBody requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET)
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .key(onlineCourse.getPath())
                .build();
        s3client.putObject(putObjectRequest, requestBody);
    }

    public void delete(UUID trainingCenterId, UUID courseId) {
        try {
            OnlineCourse onlineCourse = onlineCourseService.getTrainingCenterCourseById(trainingCenterId, courseId);
            s3client.deleteObject(b -> b.bucket(BUCKET).key(onlineCourse.getPath()));
        } catch (ResourceNotFoundException ignore) {}
    }

    @SneakyThrows
    public byte[] download(UUID trainingCenterId, UUID courseId) {
        OnlineCourse onlineCourse = onlineCourseService.getTrainingCenterCourseById(trainingCenterId, courseId);
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(onlineCourse.getPath())
                    .build();
            ResponseBytes<GetObjectResponse> bytes = s3client.getObject(getObjectRequest, ResponseTransformer.toBytes());
            return bytes.asByteArray();
        } catch (Exception e) {
            throw new ResourceNotFoundException("ONLINE_COURSE_FILE_ID={" + onlineCourse.getId() + "} not found.");
        }
    }
}
