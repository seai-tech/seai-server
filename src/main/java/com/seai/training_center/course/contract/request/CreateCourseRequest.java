package com.seai.training_center.course.contract.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

@Data
public class CreateCourseRequest {

    private String name;

    private Date startDate;

    private Date endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal price;
}
