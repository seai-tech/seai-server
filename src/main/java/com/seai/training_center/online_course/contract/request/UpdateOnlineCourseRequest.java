package com.seai.training_center.online_course.contract.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateOnlineCourseRequest {
    private String name;

    private String description;

    private Duration duration;

    private String path;
}
