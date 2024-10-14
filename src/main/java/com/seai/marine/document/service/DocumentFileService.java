package com.seai.marine.document.service;

import com.seai.service.file_service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class DocumentFileService extends FileService {

    @Value("${scanner.aws.bucket.name}")
    private String bucketName;

    public DocumentFileService(S3Client s3client) {
        super(s3client);
    }

    @Override
    protected String getBucketName() {
        return bucketName;
    }
}
