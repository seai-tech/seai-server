package com.seai.manning_agent.manning_agent.contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManningAgentAuthenticationResponse {

    private String manningAgentId;

    private String accessToken;
}
