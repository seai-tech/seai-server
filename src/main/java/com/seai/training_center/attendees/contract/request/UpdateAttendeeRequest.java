package com.seai.training_center.attendees.contract.request;

import lombok.Data;

@Data
public class UpdateAttendeeRequest {

    private String name;

    private String email;

    private String telephone;

    private String remark;

    private Boolean isWaiting;
}

