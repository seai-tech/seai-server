package com.seai.training_center.user.controller;

import com.seai.marine.user.contract.request.UserUpdateRequest;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.mapper.UserAuthenticationMapper;
import com.seai.marine.user.mapper.UserMapper;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.user.repository.UserRepository;
import com.seai.training_center.user.contract.request.CreateTrainingCenterRequest;
import com.seai.training_center.user.contract.request.UpdateTrainingCenterRequest;
import com.seai.training_center.user.contract.response.GetTrainingCenterResponse;
import com.seai.training_center.user.mapper.TrainingCenterAuthenticationMapper;
import com.seai.training_center.user.mapper.TrainingCenterMapper;
import com.seai.training_center.user.model.TrainingCenter;
import com.seai.training_center.user.repository.TrainingCenterRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final TrainingCenterAuthenticationMapper trainingCenterAuthenticationMapper;

//    @PutMapping("/training-centers/{trainingCenterId}")
//    public void updateTrainingCenter(@RequestBody @Valid UpdateTrainingCenterRequest updateTrainingCenterRequest, @PathVariable UUID trainingCenterId) {
//        trainingCenterRepository.findById(userId);
//        User user = userMapper.map(userRegisterRequest);
//        userRepository.update(userId, user);
//    }

    @GetMapping("/training-centers/{trainingCenterId}")
    public GetTrainingCenterResponse getTrainingCenter(@PathVariable UUID userId) {
        TrainingCenter trainingCenter = trainingCenterRepository.findById(userId);
        return trainingCenterMapper.map(trainingCenter);
    }

    @PostMapping("/training-centers")
    public void createTrainingCenter(@RequestBody @Valid CreateTrainingCenterRequest createTrainingCenterRequest) {
        UUID id = UUID.randomUUID();
        UserAuthentication userAuthentication = trainingCenterAuthenticationMapper.map(createTrainingCenterRequest);
        userAuthenticationRepository.save(userAuthentication, id);
        TrainingCenter trainingCenter = trainingCenterMapper.map(createTrainingCenterRequest);
        trainingCenterRepository.save(trainingCenter, id);
    }
}