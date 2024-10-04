package com.seai.service.file_service;

import com.seai.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class FileService {

    private final S3Client s3client;

    protected abstract String getBucketName();

    @SneakyThrows
    public void upload(MultipartFile multipartFile, String path) {
        RequestBody requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(getBucketName())
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .key(path)
                .build();
        s3client.putObject(putObjectRequest, requestBody);
    }

    @SneakyThrows
    public byte[] download(String filePath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(getBucketName())
                    .key(filePath)
                    .build();
            ResponseBytes<GetObjectResponse> bytes = s3client.getObject(getObjectRequest, ResponseTransformer.toBytes());
            return bytes.asByteArray();
        } catch (Exception e) {
            throw new ResourceNotFoundException("File not found: " + filePath);
        }
    }

    public void delete(String path) {
        s3client.deleteObject(b -> b.bucket(getBucketName()).key(path));
    }

    public void deleteAllForUser(UUID uuid) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(getBucketName())
                .prefix(uuid.toString() + "/")
                .build();

        ListObjectsV2Response listResponse = s3client.listObjectsV2(listRequest);
        List<ObjectIdentifier> objectsToDelete = listResponse.contents().stream()
                .map(object -> ObjectIdentifier.builder().key(object.key()).build())
                .collect(Collectors.toList());

        if (!objectsToDelete.isEmpty()) {
            DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                    .bucket(getBucketName())
                    .delete(Delete.builder().objects(objectsToDelete).build())
                    .build();
            s3client.deleteObjects(deleteRequest);
        }
    }
}
