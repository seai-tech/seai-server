package com.seai.marine.document.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.seai.exception.ResourceNotFoundException;
import com.seai.marine.document.model.MarineDocument;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentFileService {

    private static final String BUCKET = "marine-docs1";

    private final AmazonS3 s3client;

    @SneakyThrows
    public void upload(MultipartFile multipartFile, String path) {
        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(multipartFile.getContentType());
        data.setContentLength(multipartFile.getSize());
        s3client.putObject(BUCKET, path, multipartFile.getInputStream(), data);
    }

    public void delete(String path) {
        s3client.deleteObject(BUCKET, path);
    }

    @SneakyThrows
    public byte[] download(MarineDocument marineDocument) {

        GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET, marineDocument.getPath());
        try {
            S3Object s3Object = s3client.getObject(getObjectRequest);
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(objectInputStream);
        } catch (Exception e) {
            throw new ResourceNotFoundException("File for document not found: " + marineDocument.getId());
        }
    }

    public void deleteAllForUser(UUID uuid) {

        ListObjectsV2Result listObjectsV2Result = s3client.listObjectsV2(BUCKET, uuid.toString() + "/");
        List<DeleteObjectsRequest.KeyVersion> objectsToDelete = listObjectsV2Result.getObjectSummaries().stream().map(s -> new DeleteObjectsRequest.KeyVersion(s.getKey())).toList();

        // Delete objects
        if (!objectsToDelete.isEmpty()) {
            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(BUCKET);
            deleteRequest.setKeys(objectsToDelete);
            s3client.deleteObjects(deleteRequest);
        }
    }
}
