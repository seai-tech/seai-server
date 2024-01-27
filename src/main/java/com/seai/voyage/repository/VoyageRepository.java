package com.seai.voyage.repository;


import com.seai.voyage.model.Rank;
import com.seai.voyage.model.Voyage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VoyageRepository {

    private static final String SAVE_VOYAGE_QUERY = "INSERT INTO voyages (id, user_id, vessel_name, rank, imo_number, joining_port, joining_date, leaving_port, leaving_date, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_VOYAGE_QUERY = "UPDATE voyages SET vessel_name=?, rank=?, imo_number=?, joining_port=?, joining_date=?, leaving_port=?, leaving_date=?, remarks=? WHERE id=? AND user_id=?";

    private static final String FIND_VOYAGES_BY_USER_ID_QUERY = "SELECT id, vessel_name, rank, imo_number, joining_port, joining_date, leaving_port, leaving_date, remarks FROM voyages WHERE user_id= ?";

    private static final String DELETE_VOYAGES_QUERY = "DELETE FROM voyages WHERE user_id=?  AND id=?";

    private final JdbcTemplate jdbcTemplate;

    public Voyage save(Voyage voyage, UUID userId) {
        UUID id = UUID.randomUUID();
        voyage.setId(id);
        jdbcTemplate.update(SAVE_VOYAGE_QUERY,
                id,
                userId,
                voyage.getVesselName(),
                voyage.getRank().toString(),
                voyage.getImoNumber(),
                voyage.getJoiningPort(),
                Timestamp.from(voyage.getJoiningDate().toInstant()),
                voyage.getLeavingPort(),
                Timestamp.from(voyage.getLeavingDate().toInstant()),
                voyage.getRemarks());
        return voyage;
    }



    public void update(Voyage voyage, UUID userId, UUID id) {
        jdbcTemplate.update(UPDATE_VOYAGE_QUERY,
                voyage.getVesselName(),
                voyage.getRank().toString(),
                voyage.getImoNumber(),
                voyage.getJoiningPort(),
                voyage.getJoiningDate(),
                voyage.getLeavingPort(),
                voyage.getLeavingDate(),
                voyage.getRemarks(),
                id.toString(),
                userId.toString());
    }

    public List<Voyage> findByUserId(UUID userId) {
        return jdbcTemplate.query(FIND_VOYAGES_BY_USER_ID_QUERY,
                (rs, rowNum) -> new Voyage(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("vessel_name"),
                        Rank.valueOf(rs.getString("rank")),
                        rs.getString("imo_number"),
                        rs.getString("joining_port"),
                        new Date(rs.getObject("joining_date", java.sql.Date.class).getTime()),
                        rs.getString("leaving_port"),
                        new Date(rs.getObject("leaving_date", java.sql.Date.class).getTime()),
                        rs.getString("remarks")),
                userId.toString());

    }

    public void delete(UUID userId, UUID voyageId) {
        jdbcTemplate.update(DELETE_VOYAGES_QUERY, userId.toString(), voyageId.toString());
    }
}
