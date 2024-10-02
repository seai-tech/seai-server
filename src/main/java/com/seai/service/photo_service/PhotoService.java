package com.seai.service.photo_service;

import com.seai.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public abstract class PhotoService {

    @Value("${scanner.aws.bucket.name}")
    protected String bucketName;

    @Value("${user.photo.cache.maxAge}")
    protected long photoCacheMaxAge;

    protected final S3Client s3client;

    protected abstract String getPhotoPath(UUID id);

    @SneakyThrows
    public void updatePhoto(UUID id, MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        String photoPath = getPhotoPath(id);

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
    public ResponseEntity<byte[]> downloadPhoto(UUID id) {
        String photoPath = getPhotoPath(id);
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
            throw new ResourceNotFoundException(String.format("Photo not found for ID: %s", id.toString()));
        }
    }

    public void deletePhoto(UUID id) {
        String photoPath = getPhotoPath(id);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(photoPath)
                .build();
        s3client.deleteObject(deleteObjectRequest);
    }
}
