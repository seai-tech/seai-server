package com.seai.training_center.online_course.contract.response;

import lombok.Data;

import java.time.Duration;

@Data
public class GetOnlineCourseResponse {

    private String name;

    private String description;

    private Duration duration;
}
