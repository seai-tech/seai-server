package com.seai.training_center.course.mapper;

import com.seai.training_center.course.contract.request.CreateCourseRequest;
import com.seai.training_center.course.contract.request.UpdateCourseRequest;
import com.seai.training_center.course.contract.response.CreateCourseResponse;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import com.seai.training_center.course.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    Course map(CreateCourseRequest createCourseRequest);
    Course map(UpdateCourseRequest updateCourseRequest);
    GetCourseResponse map(Course course);
    CreateCourseResponse mapToCreateCourseResponse(Course course);
}
