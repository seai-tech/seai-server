package com.seai.training_center.online_course.service;

import com.seai.service.file_service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class OnlineCourseFileService extends FileService {

    @Value("${scanner.aws.bucket.name}")
    private String bucketName;

    public OnlineCourseFileService(S3Client s3Client) {
        super(s3Client);
    }

    @Override
    protected String getBucketName() {
        return bucketName;
    }
}
