package com.seai.manning_agent.sailor.repository;

import com.seai.marine.user.model.Rank;
import com.seai.marine.user.model.Status;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.VesselType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ManningAgentSailorRepository {

    private static final String REGISTER_SAILOR_QUERY = "INSERT INTO sailors (user_id, first_name, last_name, rank, present_employer, date_of_birth, manning_agents, status, vessel_type, home_airport, readiness_date, contract_duration, phone, display_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_SAILOR_BY_ID_QUERY = "SELECT user_id, first_name, last_name,rank,present_employer, date_of_birth, manning_agents, status, vessel_type, home_airport, readiness_date, contract_duration, phone, display_id FROM sailors WHERE manning_agents=? AND user_id= ?";

    private static final String FIND_SAILOR_BY_DISPLAY_ID_QUERY = "SELECT user_id, first_name, last_name,rank,present_employer, date_of_birth, manning_agents, status, vessel_type, home_airport, readiness_date, contract_duration, phone, display_id FROM sailors WHERE manning_agents=? AND display_id= ?";

    private static final String FIND_ALL_SAILORS_FOR_MANNING_CENTER = "SELECT user_id, first_name, last_name,rank,present_employer, date_of_birth, manning_agents, status, vessel_type, home_airport, readiness_date, contract_duration, phone, display_id FROM sailors WHERE manning_agents=?";

    private static final String DELETE_SAILOR_QUERY = "DELETE FROM sailors WHERE manning_agents=? AND user_id=?";

    private final JdbcTemplate jdbcTemplate;

    public List<User> getAllSailors(UUID manningAgentId) {
        return jdbcTemplate.query(FIND_ALL_SAILORS_FOR_MANNING_CENTER, getSailorRowMapper() ,manningAgentId.toString());
    }

    public Optional<User> getSailorById(UUID manningAgentId, UUID userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_SAILOR_BY_ID_QUERY,
                    getSailorRowMapper(),
                    manningAgentId.toString(),
                    userId.toString()
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findSailorByDisplayId(UUID manningAgentId, String userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_SAILOR_BY_DISPLAY_ID_QUERY,
                    getSailorRowMapper(),
                    manningAgentId.toString(),
                    userId
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public User createSailor(User user) {
        jdbcTemplate.update(REGISTER_SAILOR_QUERY,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                Optional.ofNullable(user.getRank()).map(Enum::toString).orElse(null),
                user.getPresentEmployer(),
                user.getDateOfBirth(),
                user.getManningAgents(),
                Optional.ofNullable(user.getStatus()).map(Enum::toString).orElse(null),
                Optional.ofNullable(user.getVesselType()).map(Enum::toString).orElse(null),
                user.getHomeAirport(),
                user.getReadinessDate(),
                user.getContractDuration(),
                user.getPhone(),
                user.getDisplayId());
        return user;
    }

    public void delete(UUID manningAgentId, UUID sailorId) {
        jdbcTemplate.update(DELETE_SAILOR_QUERY, manningAgentId.toString(), sailorId.toString());
    }


    private RowMapper<User> getSailorRowMapper() {
        return (rs, rowNum) -> new User(
                UUID.fromString(rs.getString("user_id")),
                rs.getString("first_name"),
                rs.getString("last_name"),
                Optional.ofNullable(rs.getString("rank"))
                        .map(Rank::valueOf).orElse(null),
                rs.getString("present_employer"),
                Optional.ofNullable(rs.getObject("date_of_birth", java.sql.Date.class))
                        .map(s -> new Date(s.getTime())).orElse(null),
                rs.getString("manning_agents"),
                Optional.ofNullable(rs.getString("status"))
                        .map(Status::valueOf).orElse(null),
                Optional.ofNullable(rs.getString("vessel_type"))
                        .map(VesselType::valueOf).orElse(null),
                rs.getString("home_airport"),
                Optional.ofNullable(rs.getObject("readiness_date", java.sql.Date.class))
                        .map(s -> new Date(s.getTime())).orElse(null),
                rs.getInt("contract_duration"),
                rs.getString("phone"),
                rs.getString("display_id"));
    }
}
