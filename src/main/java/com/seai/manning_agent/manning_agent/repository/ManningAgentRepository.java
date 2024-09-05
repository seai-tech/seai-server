package com.seai.manning_agent.manning_agent.repository;

import com.seai.manning_agent.manning_agent.model.ManningAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class ManningAgentRepository {

    private static final String REGISTER_MANNING_AGENT_QUERY = "INSERT INTO manning_agents (id, name, address, website, telephone_1, telephone_2, display_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_DISPLAY_ID = "SELECT * FROM manning_agents WHERE display_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public void save(ManningAgent manningAgent) {
        jdbcTemplate.update(REGISTER_MANNING_AGENT_QUERY,
                manningAgent.getId(),
                manningAgent.getNameOfOrganization(),
                manningAgent.getAddress(),
                manningAgent.getWebsite(),
                manningAgent.getTelephone1(),
                manningAgent.getTelephone2(),
                manningAgent.getDisplayId()
        );
    }

    public Optional<ManningAgent> findByDisplayId(String displayId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_BY_DISPLAY_ID,
                    getManningAgentRowMapper(),
                    displayId
            ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    private RowMapper<ManningAgent> getManningAgentRowMapper() {
        return (rs, rowNum) -> new ManningAgent(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("website"),
                rs.getString("telephone_1"),
                rs.getString("telephone_2"),
                rs.getString("display_id")
        );
    }
}
