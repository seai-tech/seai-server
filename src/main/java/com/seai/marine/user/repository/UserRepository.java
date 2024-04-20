package com.seai.marine.user.repository;

import com.seai.exception.ResourceNotFoundException;
import com.seai.marine.user.model.Rank;
import com.seai.marine.user.model.Status;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.VesselType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String FIND_USER_BY_ID_QUERY = "SELECT user_id, first_name, last_name,rank," +
            " present_employer, date_of_birth, manning_agents, status," +
            " vessel_type, home_airport, readiness_date, contract_duration, phone FROM sailors WHERE user_id= ?";

    private static final String REGISTER_USER_QUERY = "INSERT INTO sailors (user_id, first_name, last_name, rank, present_employer, date_of_birth," +
            " manning_agents, status, vessel_type, home_airport, readiness_date, contract_duration, phone)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_USER_QUERY = "UPDATE sailors SET first_name=?, last_name=?," +
            " rank=?, present_employer=?, date_of_birth=?, manning_agents=?, status=?, vessel_type=?, home_airport=?, readiness_date=?, contract_duration=?, phone=? WHERE user_id=?";

    private static final String DELETE_USER_QUERY = "DELETE FROM sailors WHERE user_id=?";

    private final JdbcTemplate jdbcTemplate;

    public User findById(UUID id) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_ID_QUERY,
                    getUserRowMapper(), id.toString());
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("User with id not found : " + id);
        }
    }

    private RowMapper<User> getUserRowMapper() {
        return (rs, rowNum) -> new User(UUID.fromString(rs.getString("user_id")),
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
                rs.getString("phone"));
    }

    public void save(User user, UUID id) {
        jdbcTemplate.update(REGISTER_USER_QUERY, id,
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
                user.getPhone());
    }

    public void update(UUID userId, User user) {
        jdbcTemplate.update(UPDATE_USER_QUERY,
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
                userId.toString());
    }

    public void delete(UUID uuid) {
        jdbcTemplate.update(DELETE_USER_QUERY, uuid.toString());
    }
}
