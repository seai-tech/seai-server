package com.seai.training_center.attendees.mapper;

import com.seai.training_center.attendees.contract.request.CreateAttendeeRequest;
import com.seai.training_center.attendees.contract.request.UpdateAttendeeRequest;
import com.seai.training_center.attendees.contract.response.GetAttendeeResponse;
import com.seai.training_center.attendees.model.Attendee;
import com.seai.training_center.course.contract.request.CreateCourseRequest;
import com.seai.training_center.course.contract.response.GetCourseResponse;
import com.seai.training_center.course.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttendeeMapper {

    Attendee map(CreateAttendeeRequest createAttendeeRequest);
    GetAttendeeResponse map(Attendee attendee);
    Attendee map(UpdateAttendeeRequest updateAttendeeRequest);
}
