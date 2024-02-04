package com.seai.training_center.training_center.controller;

import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.training_center.training_center.contract.request.CreateTrainingCenterRequest;
import com.seai.training_center.training_center.contract.response.GetTrainingCenterResponse;
import com.seai.training_center.training_center.mapper.TrainingCenterAuthenticationMapper;
import com.seai.training_center.training_center.mapper.TrainingCenterMapper;
import com.seai.training_center.training_center.model.TrainingCenter;
import com.seai.training_center.training_center.repository.TrainingCenterAuthenticationRepository;
import com.seai.training_center.training_center.repository.TrainingCenterRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TrainingCenterController {

    private final TrainingCenterMapper trainingCenterMapper;
    private final TrainingCenterRepository trainingCenterRepository;
    private final TrainingCenterAuthenticationRepository trainingCenterAuthenticationRepository;
    private final TrainingCenterAuthenticationMapper trainingCenterAuthenticationMapper;

    @GetMapping("/training-centers/{trainingCenterId}")
    public GetTrainingCenterResponse getTrainingCenter(@PathVariable UUID trainingCenterId) {
        TrainingCenter trainingCenter = trainingCenterRepository.findById(trainingCenterId);
        return trainingCenterMapper.map(trainingCenter);
    }

    @PostMapping("/training-centers")
    public void createTrainingCenter(@RequestBody @Valid CreateTrainingCenterRequest createTrainingCenterRequest) {
        UUID id = UUID.randomUUID();
        UserAuthentication userAuthentication = trainingCenterAuthenticationMapper.map(createTrainingCenterRequest);
        trainingCenterAuthenticationRepository.save(userAuthentication, id);
        TrainingCenter trainingCenter = trainingCenterMapper.map(createTrainingCenterRequest);
        trainingCenterRepository.save(trainingCenter, id);
    }
}