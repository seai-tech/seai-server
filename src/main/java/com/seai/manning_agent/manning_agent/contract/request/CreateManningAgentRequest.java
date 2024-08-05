package com.seai.manning_agent.manning_agent.contract.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateManningAgentRequest {

    private String nameOfOrganization;

    private String email;

    private String password;

    private String address;

    private String telephone1;

    private String telephone2;

    private String website;
}
