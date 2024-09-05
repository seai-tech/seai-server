package com.seai.training_center.training_center.controller;

import com.seai.training_center.training_center.contract.request.CreateTrainingCenterRequest;
import com.seai.training_center.training_center.contract.response.GetTrainingCenterResponse;
import com.seai.training_center.training_center.model.TrainingCenter;
import com.seai.training_center.training_center.service.TrainingCenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/training-centers")
public class TrainingCenterController {

    private final TrainingCenterService trainingCenterService;

    @GetMapping("/{trainingCenterId}")
    public Optional<GetTrainingCenterResponse> getTrainingCenterById(@PathVariable UUID trainingCenterId) {
        return trainingCenterService.getTrainingCenterById(trainingCenterId);
    }

    @GetMapping("/display-id/{trainingCenterDisplayId}")
    public Optional<GetTrainingCenterResponse> getTrainingCenterByDisplayId(@PathVariable String trainingCenterDisplayId) {
        return trainingCenterService.getTrainingCenterByDisplayId(trainingCenterDisplayId);
    }

    @PostMapping
    public TrainingCenter createTrainingCenter(@RequestBody @Valid CreateTrainingCenterRequest createTrainingCenterRequest) {
        return trainingCenterService.createTrainingCenter(createTrainingCenterRequest);
    }
}
