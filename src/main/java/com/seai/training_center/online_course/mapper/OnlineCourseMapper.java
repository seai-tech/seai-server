package com.seai.training_center.online_course.mapper;

import com.seai.training_center.online_course.contract.request.CreateOnlineCourseRequest;
import com.seai.training_center.online_course.contract.request.UpdateOnlineCourseRequest;
import com.seai.training_center.online_course.contract.response.CreateOnlineCourseResponse;
import com.seai.training_center.online_course.contract.response.GetOnlineCourseResponse;
import com.seai.training_center.online_course.contract.response.UpdateOnlineCourseResponse;
import com.seai.training_center.online_course.model.OnlineCourse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OnlineCourseMapper {

    OnlineCourse map(CreateOnlineCourseRequest createOnlineCourseRequest);

    OnlineCourse map(UpdateOnlineCourseRequest updateOnlineCourseRequest);

    GetOnlineCourseResponse mapToGetResponse(OnlineCourse onlineCourse);

    CreateOnlineCourseResponse mapToCreateResponse(OnlineCourse onlineCourse);

    UpdateOnlineCourseResponse mapToUpdateResponse(OnlineCourse onlineCourse);
}
