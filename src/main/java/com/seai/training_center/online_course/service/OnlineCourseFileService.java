package com.seai.training_center.online_course.service;

import com.seai.common.exception.ResourceNotFoundException;
import com.seai.training_center.online_course.model.OnlineCourse;
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

@Service
@RequiredArgsConstructor
public class OnlineCourseFileService {

    private static final String BUCKET = "training-center-online-courses";

    private final S3Client s3client;

    @SneakyThrows
    public void upload(MultipartFile multipartFile, String path) {
        RequestBody requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET)
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .key(path)
                .build();
        s3client.putObject(putObjectRequest, requestBody);
    }

    public void delete(String path) {
        s3client.deleteObject(b-> b.bucket(BUCKET).key(path));
    }

    @SneakyThrows
    public byte[] download(OnlineCourse onlineCourse) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(onlineCourse.getPath())
                    .build();
            ResponseBytes<GetObjectResponse> bytes = s3client.getObject(getObjectRequest, ResponseTransformer.toBytes());
            return bytes.asByteArray();
        } catch (Exception e) {
            throw new ResourceNotFoundException("File for online course not found: " + onlineCourse.getId());
        }
    }
}
