package com.seai.training_center.training_center.mapper;

import com.seai.marine.user.model.UserAuthentication;
import com.seai.training_center.training_center.contract.request.CreateTrainingCenterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainingCenterAuthenticationMapper {

    UserAuthentication map(CreateTrainingCenterRequest createTrainingCenterRequest);
}
