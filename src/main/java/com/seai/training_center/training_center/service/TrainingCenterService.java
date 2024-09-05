package com.seai.training_center.training_center.service;

import com.seai.common.display_id_service.DisplayIdService;
import com.seai.common.exception.ResourceNotFoundException;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.training_center.training_center.contract.request.CreateTrainingCenterRequest;
import com.seai.training_center.training_center.contract.response.GetTrainingCenterResponse;
import com.seai.training_center.training_center.mapper.TrainingCenterAuthenticationMapper;
import com.seai.training_center.training_center.mapper.TrainingCenterMapper;
import com.seai.training_center.training_center.model.TrainingCenter;
import com.seai.training_center.training_center.repository.TrainingCenterAuthenticationRepository;
import com.seai.training_center.training_center.repository.TrainingCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingCenterService {

    private final TrainingCenterMapper trainingCenterMapper;

    private final TrainingCenterRepository trainingCenterRepository;

    private final TrainingCenterAuthenticationRepository trainingCenterAuthenticationRepository;

    private final TrainingCenterAuthenticationMapper trainingCenterAuthenticationMapper;

    private final DisplayIdService displayIdService;

    @Value("${training-center.prefix.display-id}")
    private String displayIdPrefix;

    public Optional<GetTrainingCenterResponse> getTrainingCenterById(UUID trainingCenterId ) throws ResourceNotFoundException {
        TrainingCenter trainingCenter = trainingCenterRepository.findById(trainingCenterId).orElseThrow(() ->
                new ResourceNotFoundException("Training center with id" + trainingCenterId + "not found"));
        return Optional.ofNullable(trainingCenterMapper.map(trainingCenter));
    }

    public Optional<GetTrainingCenterResponse> getTrainingCenterByDisplayId(String displayId ) throws ResourceNotFoundException {
        TrainingCenter trainingCenter = trainingCenterRepository.findTrainingCenterByDisplayId(displayId).orElseThrow(() ->
                new ResourceNotFoundException("Training center with display id" + displayId + "not found"));
        return Optional.ofNullable(trainingCenterMapper.map(trainingCenter));
    }

    public TrainingCenter createTrainingCenter(CreateTrainingCenterRequest createTrainingCenterRequest) {
        UUID id = UUID.randomUUID();
        TrainingCenter trainingCenter = trainingCenterMapper.map(createTrainingCenterRequest);
        trainingCenter.setId(id);
        trainingCenter.setDisplayId(displayIdService.generateDisplayId(displayIdPrefix, null));
        UserAuthentication userAuthentication = trainingCenterAuthenticationMapper.map(createTrainingCenterRequest);
        trainingCenterAuthenticationRepository.save(userAuthentication, id);
        trainingCenterRepository.save(trainingCenter, id);
        return trainingCenter;
    }
}
