package com.seai.training_center.course.contract.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
public class GetCourseResponse {

    private UUID id;

    private UUID trainingCenterId;

    private String name;

    private Date startDate;

    private Date endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal price;

    private String currency;
}
