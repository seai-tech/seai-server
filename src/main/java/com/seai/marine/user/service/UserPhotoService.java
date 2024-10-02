package com.seai.marine.user.service;

import com.seai.service.photo_service.PhotoService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.UUID;

@Service
public class UserPhotoService extends PhotoService {

    public UserPhotoService(S3Client s3client) {
        super(s3client);
    }

    @Override
    protected String getPhotoPath(UUID userId) {
        return "users/" + userId + "/photo";
    }
}
