package com.seai.manning_agent.sailor.document.service;

import com.seai.service.file_service.FileService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class ManningAgentDocumentFileService extends FileService {

    @Value("${scanner.aws.bucket.name}")
    private String bucketName;

    public ManningAgentDocumentFileService(S3Client s3Client) {
        super(s3Client);
    }

    @Override
    protected String getBucketName() {
        return bucketName;
    }
}
