package com.seai.training_center.course.contract.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seai.training_center.course.model.CurrencyOptions;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

@Data
public class CreateCourseRequest {

    private String name;

    private Date startDate;

    private Date endDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private BigDecimal price;

    private CurrencyOptions currency;

    private Integer maxSeats;

    private String description;

    private Boolean isPublished;
}
