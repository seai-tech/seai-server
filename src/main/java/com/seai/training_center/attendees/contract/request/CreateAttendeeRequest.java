package com.seai.training_center.attendees.contract.request;


import lombok.Data;

import java.util.UUID;

@Data
public class CreateAttendeeRequest {

        private String name;

        private String email;

        private String telephone;

        private String remark;

        private Boolean isWaiting;

        private UUID userId;
    }

