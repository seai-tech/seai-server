package com.seai.training_center.training_center.service;

import com.seai.service.photo_service.PhotoService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.UUID;

@Service
public class TrainingCenterPhotoUploadService extends PhotoService {

    public TrainingCenterPhotoUploadService(S3Client s3client) {
        super(s3client);
    }

    @Override
    protected String getPhotoPath(UUID id) {
        return "trainingCenters/" + id + "/photo";
    }
}
