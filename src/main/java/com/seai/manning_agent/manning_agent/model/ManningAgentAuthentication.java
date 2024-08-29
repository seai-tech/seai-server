package com.seai.manning_agent.manning_agent.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ManningAgentAuthentication {

    private UUID id;

    private String email;

    private String password;
}
