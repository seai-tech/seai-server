package com.seai.training_center.online_course.contract.request;

import lombok.Data;

import java.time.Duration;

@Data
public class CreateOnlineCourseRequest {

    private String name;

    private String description;

    private Duration duration;
}
