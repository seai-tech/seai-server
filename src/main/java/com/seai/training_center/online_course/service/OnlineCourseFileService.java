package com.seai.training_center.online_course.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.seai.exception.ResourceNotFoundException;
import com.seai.training_center.online_course.model.OnlineCourse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OnlineCourseFileService {

    private static final String BUCKET = "training-center-online-courses";

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
    public S3ObjectInputStream download(OnlineCourse onlineCourse) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET, onlineCourse.getPath());
        try {
            S3Object s3Object = s3client.getObject(getObjectRequest);
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

            return objectInputStream;
        } catch (Exception e) {
            throw new ResourceNotFoundException("File for online course not found: " + onlineCourse.getId());
        }
    }
}
