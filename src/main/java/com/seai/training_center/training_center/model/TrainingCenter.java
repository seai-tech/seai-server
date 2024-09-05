package com.seai.training_center.training_center.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TrainingCenter {

    private UUID id;

    private String nameOfOrganization;

    private String telephone1;

    private String telephone2;

    private String telephone3;

    private String displayId;
}
