package com.seai.training_center.user.mapper;

import com.seai.training_center.user.contract.request.CreateTrainingCenterRequest;
import com.seai.training_center.user.contract.response.GetTrainingCenterResponse;
import com.seai.training_center.user.model.TrainingCenter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainingCenterMapper {

    TrainingCenter map(CreateTrainingCenterRequest createTrainingCenterRequest);

    GetTrainingCenterResponse map(TrainingCenter trainingCenter);
}
