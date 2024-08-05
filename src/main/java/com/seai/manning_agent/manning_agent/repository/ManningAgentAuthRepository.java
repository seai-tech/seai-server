package com.seai.manning_agent.manning_agent.repository;

import com.seai.manning_agent.manning_agent.model.ManningAgentAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ManningAgentAuthRepository {

    private static final String REGISTER_MANNING_AGENT_QUERY = "INSERT INTO manning_agents_auth (id, email, password)" +
            " VALUES (?, ?, ?)";

    private static final String FIND_MANNING_AGENT_BY_EMAIL_QUERY = "SELECT id, email, password FROM manning_agents_auth WHERE email= ?";

    private final PasswordEncoder encoder;

    private final JdbcTemplate jdbcTemplate;

    public void save(ManningAgentAuthentication manningAgentAuthentication) {
        jdbcTemplate.update(REGISTER_MANNING_AGENT_QUERY, manningAgentAuthentication.getId(),
                manningAgentAuthentication.getEmail(),
                encoder.encode(manningAgentAuthentication.getPassword()));
    }

    public Optional<ManningAgentAuthentication> findByEmail(String email) {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_MANNING_AGENT_BY_EMAIL_QUERY,
                    (rs, rowNum) -> new ManningAgentAuthentication(UUID.fromString(rs.getString("id")),
                            rs.getString("email"),
                            rs.getString("password")), email));
    }
}
