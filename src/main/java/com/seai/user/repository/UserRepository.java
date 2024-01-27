package com.seai.user.repository;

import com.seai.user.model.Sailor;
import com.seai.user.model.Status;
import com.seai.user.model.VesselType;
import com.seai.voyage.model.Rank;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String FIND_USER_BY_EMAIL_QUERY = "SELECT id, email, password, first_name, last_name,rank," +
            " present_employer, date_of_birth, manning_agents, status," +
            " vessel_type, home_airport, readiness_date, contract_duration FROM users WHERE email= ?";

    private static final String FIND_USER_BY_ID_QUERY = "SELECT id, email, password, first_name, last_name,rank," +
            " present_employer, date_of_birth, manning_agents, status," +
            " vessel_type, home_airport, readiness_date, contract_duration FROM users WHERE id= ?";

    private static final String REGISTER_USER_QUERY = "INSERT INTO users (id, email, password, first_name, last_name)" +
            " VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_USER_QUERY = "UPDATE users SET first_name=?, last_name=?," +
            " rank=?, present_employer=?, date_of_birth=?, manning_agents=?, status=?, vessel_type=?, home_airport=?, readiness_date=?, contract_duration=? WHERE id=?";


    private final PasswordEncoder encoder;

    private final JdbcTemplate jdbcTemplate;


    public Sailor findByEmail(String email) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL_QUERY,
                    (rs, rowNum) -> new Sailor(UUID.fromString(rs.getString("id")),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            Optional.ofNullable(rs.getString("rank"))
                                    .map(Rank::valueOf).orElse(null),
                            rs.getString("present_employer"),
                            Optional.ofNullable(rs.getObject("date_of_birth", java.sql.Date.class))
                                    .map(s-> new Date(s.getTime())).orElse(null),
                            rs.getString("manning_agents"),
                            Optional.ofNullable(rs.getString("status"))
                                    .map(Status::valueOf).orElse(null),
                            Optional.ofNullable(rs.getString("vessel_type"))
                                    .map(VesselType::valueOf).orElse(null),
                            rs.getString("home_airport"),
                            Optional.ofNullable(rs.getObject("readiness_date", java.sql.Date.class))
                                    .map(s-> new Date(s.getTime())).orElse(null),
                            rs.getInt("contract_duration")), email);
        } catch (EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("User with email not found : " + email);
        }
    }


    public Sailor findById(UUID id) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_ID_QUERY,
                    (rs, rowNum) -> new Sailor(UUID.fromString(rs.getString("id")),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            Optional.ofNullable(rs.getString("rank"))
                                    .map(Rank::valueOf).orElse(null),
                            rs.getString("present_employer"),
                            Optional.ofNullable(rs.getObject("date_of_birth", java.sql.Date.class))
                                    .map(s-> new Date(s.getTime())).orElse(null),
                            rs.getString("manning_agents"),
                            Optional.ofNullable(rs.getString("status"))
                                    .map(Status::valueOf).orElse(null),
                            Optional.ofNullable(rs.getString("vessel_type"))
                                    .map(VesselType::valueOf).orElse(null),
                            rs.getString("home_airport"),
                            Optional.ofNullable(rs.getObject("readiness_date", java.sql.Date.class))
                                    .map(s-> new Date(s.getTime())).orElse(null),
                            rs.getInt("contract_duration")), id.toString());
        } catch (EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("User with id not found : " + id);
        }
    }

    public void save(Sailor sailor) {
        jdbcTemplate.update(REGISTER_USER_QUERY, UUID.randomUUID().toString(),
                sailor.getEmail(),
                encoder.encode(sailor.getPassword()),
                sailor.getFirstName(),
                sailor.getLastName());
    }

    public void update(UUID userId, Sailor sailor) {
        jdbcTemplate.update(UPDATE_USER_QUERY,
                sailor.getFirstName(),
                sailor.getLastName(),
                Optional.ofNullable(sailor.getRank()).map(Enum::toString).orElse(null),
                sailor.getPresentEmployer(),
                sailor.getDateOfBirth(),
                sailor.getManningAgents(),
                Optional.ofNullable(sailor.getStatus()).map(Enum::toString).orElse(null),
                Optional.ofNullable(sailor.getVesselType()).map(Enum::toString).orElse(null),
                sailor.getHomeAirport(),
                sailor.getReadinessDate(),
                sailor.getContractDuration(), userId.toString());
    }
}
