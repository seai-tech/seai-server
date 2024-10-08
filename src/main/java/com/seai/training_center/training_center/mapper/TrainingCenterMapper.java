package com.seai.training_center.training_center.mapper;

import com.seai.training_center.training_center.contract.request.CreateTrainingCenterRequest;
import com.seai.training_center.training_center.contract.response.GetTrainingCenterResponse;
import com.seai.training_center.training_center.model.TrainingCenter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainingCenterMapper {

    TrainingCenter map(CreateTrainingCenterRequest createTrainingCenterRequest);

    GetTrainingCenterResponse map(TrainingCenter trainingCenter);
}
