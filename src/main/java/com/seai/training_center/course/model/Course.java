package com.seai.training_center.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    private UUID id;

    private UUID trainingCenterId;

    private String name;

    private Date startDate;

    private Date endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal price;

    private CurrencyOptions currency;

    private Integer maxSeats;

    private String description;

    private Boolean isPublished;
}
