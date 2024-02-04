package com.seai.training_center.user.repository;

import com.seai.marine.user.model.Status;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.VesselType;
import com.seai.marine.voyage.model.Rank;
import com.seai.training_center.user.model.TrainingCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TrainingCenterRepository {

    private static final String SAVE_TRAINING_CENTER_QUERY = "INSERT INTO training_centers (id, organization_name, telephone_1, telephone_2, telephone_3) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_TRAINING_CENTER_QUERY = "SELECT id, organization_name, telephone_1, telephone_2, telephone_3 FROM training_centers WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    public void save(TrainingCenter trainingCenter, UUID trainingCenterId) {
        jdbcTemplate.update(SAVE_TRAINING_CENTER_QUERY,
                trainingCenterId,
                trainingCenter.getNameOfOrganization(),
                trainingCenter.getTelephone1(),
                trainingCenter.getTelephone2(),
                trainingCenter.getTelephone3());
    }

    public TrainingCenter findById(UUID trainingCenterId) {
        return jdbcTemplate.queryForObject(FIND_TRAINING_CENTER_QUERY, getTrainingCenterRowMapper(), trainingCenterId.toString());
    }

    private RowMapper<TrainingCenter> getTrainingCenterRowMapper() {
        return (rs, rowNum) -> new TrainingCenter(UUID.fromString(rs.getString("id")),
                rs.getString("organization_name"),
                rs.getString("telephone_1"),
                rs.getString("telephone_2"),
                rs.getString("telephone_3"));

    }
}
