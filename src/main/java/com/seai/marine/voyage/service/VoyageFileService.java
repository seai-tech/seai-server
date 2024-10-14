package com.seai.marine.voyage.service;

import com.seai.service.file_service.FileService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class VoyageFileService extends FileService {

    private static final String BUCKET = "voyage-file";

    public VoyageFileService(S3Client s3Client) {
        super(s3Client);
    }


    @Override
    protected String getBucketName() {
        return BUCKET;
    }
}
