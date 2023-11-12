package com.seai.domain.user.repository;

import com.seai.domain.user.model.SeaiUser;
import com.seai.domain.user.model.Status;
import com.seai.domain.user.model.VesselType;
import com.seai.domain.voyage.model.Rank;
import com.seai.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String FIND_USER_BY_ID_QUERY = "SELECT id, email, password, first_name, last_name,rank," +
            " present_employer, date_of_birth, manning_agents, status," +
            " vessel_type, home_airport, readiness_date, contract_duration FROM users WHERE email= ?";

    private static final String SAVE_USER_QUERY = "INSERT INTO users (id, email, password, first_name, last_name," +
            " rank, present_employer, date_of_birth, manning_agents, status, vessel_type, home_airport, readiness_date, contract_duration)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    private final PasswordEncoder encoder;

    private final JdbcTemplate jdbcTemplate;


    public SeaiUser find(String userId) throws UsernameNotFoundException {
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
                            rs.getInt("contract_duration")), userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("User not found : " + userId);
        }
    }

    public void save(SeaiUser seaiUser) {
        jdbcTemplate.update(SAVE_USER_QUERY, UUID.randomUUID().toString(),
                seaiUser.getEmail(),
                encoder.encode(seaiUser.getPassword()),
                seaiUser.getFirstName(),
                seaiUser.getLastName(),
                Optional.ofNullable(seaiUser.getRank()).map(Enum::toString).orElse(null),
                seaiUser.getPresentEmployer(),
                seaiUser.getDateOfBirth(),
                seaiUser.getManningAgents(),
                Optional.ofNullable(seaiUser.getStatus()).map(Enum::toString).orElse(null),
                seaiUser.getVesselType().toString(),
                seaiUser.getHomeAirport(),
                seaiUser.getReadinessDate(),
                seaiUser.getContractDuration());
    }

    public void update(SeaiUser seaiUser) {
        jdbcTemplate.update(SAVE_USER_QUERY, UUID.randomUUID().toString(),
                seaiUser.getEmail(),
                encoder.encode(seaiUser.getPassword()),
                seaiUser.getFirstName(),
                seaiUser.getLastName(),
                seaiUser.getRank().toString(),
                seaiUser.getPresentEmployer(),
                seaiUser.getDateOfBirth(),
                seaiUser.getManningAgents(),
                seaiUser.getStatus().toString(),
                seaiUser.getVesselType().toString(),
                seaiUser.getHomeAirport(),
                seaiUser.getReadinessDate(),
                seaiUser.getContractDuration());
    }
}
