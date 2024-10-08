package com.seai.marine.document.service;

import com.seai.exception.ResourceNotFoundException;
import com.seai.marine.document.model.MarineDocument;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentFileService {

    @Value("${scanner.aws.bucket.name}")
    private String bucketName;

    private final S3Client s3client;

    @SneakyThrows
    public void upload(MultipartFile multipartFile, String path) {
        RequestBody requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .key(path)
                .build();
        s3client.putObject(putObjectRequest, requestBody);
    }

    public void delete(String path) {
        s3client.deleteObject(b-> b.bucket(bucketName).key(path));
    }

    @SneakyThrows
    public byte[] download(MarineDocument marineDocument) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(marineDocument.getPath())
                    .build();
            ResponseBytes<GetObjectResponse> bytes = s3client.getObject(getObjectRequest, ResponseTransformer.toBytes());
            return bytes.asByteArray();
        } catch (Exception e) {
            throw new ResourceNotFoundException("File for document not found: " + marineDocument.getId());
        }
    }

    public void deleteAllForUser(UUID uuid) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(uuid.toString() + "/")
                .build();

        ListObjectsV2Response listResponse = s3client.listObjectsV2(listRequest);
        List<ObjectIdentifier> objectsToDelete = listResponse.contents().stream()
                .map(object -> ObjectIdentifier.builder().key(object.key()).build())
                .collect(Collectors.toList());

        if (!objectsToDelete.isEmpty()) {
            DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(Delete.builder().objects(objectsToDelete).build())
                    .build();
            s3client.deleteObjects(deleteRequest);
        }
    }
}
