package com.seai.training_center.online_course.contract.response;

import lombok.Data;

import java.time.Duration;
import java.util.UUID;

@Data
public class CreateOnlineCourseResponse {

    private UUID id;

    private String name;

    private String description;

    private Duration duration;
}
