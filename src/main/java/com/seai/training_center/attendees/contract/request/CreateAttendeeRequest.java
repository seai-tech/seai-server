package com.seai.training_center.attendees.contract.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAttendeeRequest {

        private String name;

        private String email;

        private String telephone;

        private String remark;

        private Boolean isWaiting;

    }

