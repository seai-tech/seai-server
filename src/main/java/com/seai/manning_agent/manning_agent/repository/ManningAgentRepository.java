package com.seai.manning_agent.manning_agent.repository;

import com.seai.manning_agent.manning_agent.model.ManningAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ManningAgentRepository {

    private static final String REGISTER_MANNING_AGENT_QUERY = "INSERT INTO manning_agents (id, name, address, website, telephone_1, telephone_2) VALUES (?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;


    public void save(ManningAgent manningAgent) {
        jdbcTemplate.update(REGISTER_MANNING_AGENT_QUERY,
                manningAgent.getId(),
                manningAgent.getNameOfOrganization(),
                manningAgent.getAddress(),
                manningAgent.getWebsite(),
                manningAgent.getTelephone1(),
                manningAgent.getTelephone2()
        );
    }

}
