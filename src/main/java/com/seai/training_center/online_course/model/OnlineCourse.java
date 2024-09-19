package com.seai.training_center.online_course.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OnlineCourse {

    private UUID id;

    private UUID trainingCenterId;

    private String name;

    private String description;

    private String path;

    private Duration duration;
}
