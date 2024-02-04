package com.seai.training_center.training_center.contract.request;

import lombok.Data;

@Data
public class CreateTrainingCenterRequest {

    private String nameOfOrganization;

    private String email;

    private String password;

    private String telephone1;

    private String telephone2;

    private String telephone3;
}
