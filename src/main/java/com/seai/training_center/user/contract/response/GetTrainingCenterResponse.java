package com.seai.training_center.user.contract.response;

import lombok.Data;

import java.util.UUID;

@Data
public class GetTrainingCenterResponse {

    private UUID id;

    private String nameOfOrganization;

    private String telephone1;

    private String telephone2;

    private String telephone3;
}
