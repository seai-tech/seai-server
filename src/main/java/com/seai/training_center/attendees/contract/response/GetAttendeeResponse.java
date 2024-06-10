package com.seai.training_center.attendees.contract.response;

import lombok.Data;

import java.util.UUID;

@Data
public class GetAttendeeResponse {

        private UUID id;

        private String name;

        private String email;

        private String telephone;

        private String remark;

        private Boolean isWaiting;

        private UUID courseId;

        private UUID userId;

}
