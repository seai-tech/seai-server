package com.seai.manning_agent.sailor.next_of_kin.repository;

import com.seai.marine.next_of_kin.model.NextOfKin;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ManningAgentNextOfKinRepository {

    private static final String INSERT_NEXT_OF_KIN_QUERY = "INSERT INTO next_of_kin (id, first_name, last_name, phone, address, email, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_NEXT_OF_KIN_QUERY = "UPDATE next_of_kin SET first_name=?, last_name=?, phone=?, address=?, email=? WHERE id=? AND user_id=?";

    private static final String FIND_NEXT_OF_KIN_BY_USER_ID_QUERY = "SELECT id, first_name, last_name, phone, address, email, user_id FROM next_of_kin WHERE user_id=?";

    private static final String FIND_NEXT_OF_KIN_BY_ID_QUERY = "SELECT id, first_name, last_name, phone, address, email, user_id FROM next_of_kin WHERE id=?";

    private static final String DELETE_NEXT_OF_KIN_BY_USER_ID_QUERY = "DELETE FROM next_of_kin WHERE user_id=?";

    private static final String DELETE_NEXT_OF_KIN_QUERY = "DELETE FROM next_of_kin WHERE id=? AND user_id=?";

    private final JdbcTemplate jdbcTemplate;


    public void save(NextOfKin nextOfKin) {
        jdbcTemplate.update(INSERT_NEXT_OF_KIN_QUERY,
                nextOfKin.getId(),
                nextOfKin.getName(),
                nextOfKin.getSurname(),
                nextOfKin.getPhone(),
                nextOfKin.getAddress(),
                nextOfKin.getEmail(),
                nextOfKin.getUserId());
    }

    public void update(NextOfKin nextOfKin) {
        jdbcTemplate.update(UPDATE_NEXT_OF_KIN_QUERY,
                nextOfKin.getName(),
                nextOfKin.getSurname(),
                nextOfKin.getPhone(),
                nextOfKin.getAddress(),
                nextOfKin.getEmail(),
                nextOfKin.getId().toString(),
                nextOfKin.getUserId().toString());
    }

    public Optional<NextOfKin> findByUserId(UUID userId) {
        return jdbcTemplate.query(FIND_NEXT_OF_KIN_BY_USER_ID_QUERY,
                        getNextOfKinRowMapper(),
                        userId.toString())
                .stream()
                .findFirst();
    }

    public Optional<NextOfKin> findById(UUID nextOfKinId) {
        return jdbcTemplate.query(FIND_NEXT_OF_KIN_BY_ID_QUERY, getNextOfKinRowMapper(), nextOfKinId.toString())
                .stream()
                .findFirst();
    }

    public void deleteByUserId(UUID userId) {
        jdbcTemplate.update(DELETE_NEXT_OF_KIN_BY_USER_ID_QUERY, userId.toString());
    }

    public void delete(UUID userId, UUID nestOfKinId) {
        jdbcTemplate.update(DELETE_NEXT_OF_KIN_QUERY, nestOfKinId.toString(), userId.toString());
    }

    private RowMapper<NextOfKin> getNextOfKinRowMapper() {
        return (rs, rowNum) -> {
            NextOfKin nextOfKin = new NextOfKin();
            nextOfKin.setId(UUID.fromString(rs.getString("id")));
            nextOfKin.setName(rs.getString("first_name"));
            nextOfKin.setSurname(rs.getString("last_name"));
            nextOfKin.setPhone(rs.getString("phone"));
            nextOfKin.setAddress(rs.getString("address"));
            nextOfKin.setEmail(rs.getString("email"));
            nextOfKin.setUserId(UUID.fromString(rs.getString("user_id")
            ));
            return nextOfKin;
        };
    }
}
