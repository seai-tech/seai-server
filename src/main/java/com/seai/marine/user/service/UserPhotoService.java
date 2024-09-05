package com.seai.marine.user.service;

import com.seai.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserPhotoService {

    @Value("${scanner.aws.bucket.name}")
    private String bucketName;

    @Value("${user.photo.cache.maxAge}")
    private long photoCacheMaxAge;


    private final S3Client s3client;

    @SneakyThrows
    public void updatePhoto(UUID userId, MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        String photoPath = "users/" + userId + "/photo";

        RequestBody requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(photoPath)
                .contentType(contentType)
                .contentLength(multipartFile.getSize())
                .build();

        s3client.putObject(putObjectRequest, requestBody);
    }

    @SneakyThrows
    public ResponseEntity<byte[]> downloadPhoto(UUID userId) {

        String photoPath = "users/" + userId + "/photo";
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(photoPath)
                .build();

        try {
            ResponseBytes<GetObjectResponse> responseBytes = s3client.getObject(getObjectRequest, ResponseTransformer.toBytes());
            byte[] bytes = responseBytes.asByteArray();
            HttpHeaders httpHeaders = new HttpHeaders();
            String contentType = responseBytes.response().contentType();
            httpHeaders.setContentType(MediaType.parseMediaType(contentType));
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", "photo." + contentType.split("/")[1]);
            CacheControl cacheControl = CacheControl.maxAge(photoCacheMaxAge, TimeUnit.DAYS)
                    .noTransform()
                    .mustRevalidate();

            return ResponseEntity.ok()
                    .cacheControl(cacheControl)
                    .headers(httpHeaders)
                    .body(bytes);
        } catch (Exception e) {
            throw new ResourceNotFoundException(String.format("Photo not found for user: %s ", userId.toString()));
        }
    }

    public void deletePhoto(UUID userId) {
        String photoPath = "users/" + userId + "/photo";
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(photoPath)
                .build();
        s3client.deleteObject(deleteObjectRequest);
    }
}
