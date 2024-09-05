package com.seai.manning_agent.manning_agent.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ManningAgent {

    private UUID id;

    private String nameOfOrganization;

    private String address;

    private String website;

    private String telephone1;

    private String telephone2;

    private String displayId;
}
