package com.seai.domain.user.repository;

import com.seai.domain.user.model.SeaiUser;
import com.seai.domain.user.model.Status;
import com.seai.domain.user.model.VesselType;
import com.seai.domain.voyage.model.Rank;
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


    public SeaiUser findByEmail(String email) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL_QUERY,
                    (rs, rowNum) -> new SeaiUser(UUID.fromString(rs.getString("id")),
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


    public SeaiUser findById(UUID id) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_ID_QUERY,
                    (rs, rowNum) -> new SeaiUser(UUID.fromString(rs.getString("id")),
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

    public void save(SeaiUser seaiUser) {
        jdbcTemplate.update(REGISTER_USER_QUERY, UUID.randomUUID().toString(),
                seaiUser.getEmail(),
                encoder.encode(seaiUser.getPassword()),
                seaiUser.getFirstName(),
                seaiUser.getLastName());
    }

    public void update(UUID userId, SeaiUser seaiUser) {
        jdbcTemplate.update(UPDATE_USER_QUERY,
                seaiUser.getFirstName(),
                seaiUser.getLastName(),
                Optional.ofNullable(seaiUser.getRank()).map(Enum::toString).orElse(null),
                seaiUser.getPresentEmployer(),
                seaiUser.getDateOfBirth(),
                seaiUser.getManningAgents(),
                Optional.ofNullable(seaiUser.getStatus()).map(Enum::toString).orElse(null),
                Optional.ofNullable(seaiUser.getVesselType()).map(Enum::toString).orElse(null),
                seaiUser.getHomeAirport(),
                seaiUser.getReadinessDate(),
                seaiUser.getContractDuration(), userId.toString());
    }
}
