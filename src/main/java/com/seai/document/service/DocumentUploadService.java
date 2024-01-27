package com.seai.document.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentUploadService {

    private static final String BUCKET = "marine-docs1";

    private final AmazonS3 s3client;

    @SneakyThrows
    public void upload(MultipartFile multipartFile, String path) {
        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(multipartFile.getContentType());
        data.setContentLength(multipartFile.getSize());
        s3client.putObject(BUCKET, path, multipartFile.getInputStream(), data);
    }

    public void deleteFile(String path) {
        s3client.deleteObject(BUCKET, path);
    }

    @SneakyThrows
    public byte[] downloadDocument(String path) {

        GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET, path);

        S3Object s3Object = s3client.getObject(getObjectRequest);
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

        return IOUtils.toByteArray(objectInputStream);
    }
}
