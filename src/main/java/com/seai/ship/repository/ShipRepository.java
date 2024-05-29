package com.seai.ship.repository;

import com.seai.exception.ResourceNotFoundException;
import com.seai.ship.model.Ship;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ShipRepository {

    private static final String FIND_SHIP_BY_ID_QUERY = "SELECT id, imo_number, vessel_name, ship_type, flag, homeport, gross_tonnage, summer_deadweight_t, " +
            "length_overall_m, beam_m, draught_m, year_of_build, builder, place_of_build, yard, teu, crude_oil_bbl, gas_m3, grain, bale, classification_society, " +
            "registered_owner, owner_address, owner_website, owner_email, manager, manager_address, manager_website, manager_email FROM ships WHERE id= ?";

    private static final String FIND_ALL_SHIPS_QUERY = "SELECT id, imo_number, vessel_name, ship_type, flag, homeport, gross_tonnage, summer_deadweight_t, " +
            "length_overall_m, beam_m, draught_m, year_of_build, builder, place_of_build, yard, teu, crude_oil_bbl, gas_m3, grain, bale, classification_society, " +
            "registered_owner, owner_address, owner_website, owner_email, manager, manager_address, manager_website, manager_email FROM ships";

    private static final String SAVE_SHIP_QUERY = "INSERT INTO ships (id, imo_number, vessel_name, ship_type, flag, homeport, gross_tonnage, summer_deadweight_t, " +
            "length_overall_m, beam_m, draught_m, year_of_build, builder, place_of_build, yard, teu, crude_oil_bbl, gas_m3, grain, bale, classification_society, " +
            "registered_owner, owner_address, owner_website, owner_email, manager, manager_address, manager_website, manager_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SHIP_QUERY = "UPDATE ships SET imo_number=?, vessel_name=?, ship_type=?, flag=?, homeport=?, gross_tonnage=?, summer_deadweight_t=?, " +
            "length_overall_m=?, beam_m=?, draught_m=?, year_of_build=?, builder=?, place_of_build=?, yard=?, teu=?, crude_oil_bbl=?, gas_m3=?, grain=?, bale=?, classification_society=?, " +
            "registered_owner=?, owner_address=?, owner_website=?, owner_email=?, manager=?, manager_address=?, manager_website=?, manager_email=? WHERE id=?";

    private static final String DELETE_SHIP_QUERY = "DELETE FROM ships WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    public Ship findById(UUID id) {
        try {
            return jdbcTemplate.queryForObject(FIND_SHIP_BY_ID_QUERY, getShipRowMapper(), id.toString());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Ship with id " + id + " not found");
        }
    }

    public List<Ship> findAll(String vesselName, String owner, String shipType) {
        StringBuilder queryBuilder = new StringBuilder(FIND_ALL_SHIPS_QUERY);
        List<Object> queryParams = new ArrayList<>();
        if (vesselName != null || owner != null || shipType != null) {
            queryBuilder.append(" WHERE ");
            boolean firstCondition = true;
            if (vesselName != null) {
                queryBuilder.append("vessel_name = ?");
                queryParams.add(vesselName);
                firstCondition = false;
            }
            if (owner != null) {
                if (!firstCondition) queryBuilder.append(" AND ");
                queryBuilder.append("registered_owner = ?");
                queryParams.add(owner);
                firstCondition = false;
            }
            if (shipType != null) {
                if (!firstCondition) queryBuilder.append(" AND ");
                queryBuilder.append("ship_type = ?");
                queryParams.add(shipType);
            }
        }

        return jdbcTemplate.query(queryBuilder.toString(), queryParams.toArray(), getShipRowMapper());
    }


    public Ship save(Ship ship) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update(SAVE_SHIP_QUERY,
                id.toString(),
                ship.getImoNumber(),
                ship.getVesselName(),
                ship.getShipType(),
                ship.getFlag(),
                ship.getHomeport(),
                ship.getGrossTonnage(),
                ship.getSummerDeadweight(),
                ship.getLengthOverall(),
                ship.getBeam(),
                ship.getDraught(),
                ship.getYearOfBuild(),
                ship.getBuilder(),
                ship.getPlaceOfBuild(),
                ship.getYard(),
                ship.getTeu(),
                ship.getCrudeOil(),
                ship.getGasCapacity(),
                ship.getGrain(),
                ship.getBale(),
                ship.getClassificationSociety(),
                ship.getRegisteredOwner(),
                ship.getOwnerAddress(),
                ship.getOwnerWebsite(),
                ship.getOwnerEmail(),
                ship.getManager(),
                ship.getManagerAddress(),
                ship.getManagerWebsite(),
                ship.getManagerEmail());
        ship.setId(id.toString());
        return ship;
    }

    public void update(Ship ship) {
        jdbcTemplate.update(UPDATE_SHIP_QUERY,
                ship.getImoNumber(),
                ship.getVesselName(),
                ship.getShipType(),
                ship.getFlag(),
                ship.getHomeport(),
                ship.getGrossTonnage(),
                ship.getSummerDeadweight(),
                ship.getLengthOverall(),
                ship.getBeam(),
                ship.getDraught(),
                ship.getYearOfBuild(),
                ship.getBuilder(),
                ship.getPlaceOfBuild(),
                ship.getYard(),
                ship.getTeu(),
                ship.getCrudeOil(),
                ship.getGasCapacity(),
                ship.getGrain(),
                ship.getBale(),
                ship.getClassificationSociety(),
                ship.getRegisteredOwner(),
                ship.getOwnerAddress(),
                ship.getOwnerWebsite(),
                ship.getOwnerEmail(),
                ship.getManager(),
                ship.getManagerAddress(),
                ship.getManagerWebsite(),
                ship.getManagerEmail(),
                ship.getId());
    }

    public void delete(UUID id) {
        jdbcTemplate.update(DELETE_SHIP_QUERY, id.toString());
    }

    private RowMapper<Ship> getShipRowMapper() {
        return (rs, rowNum) -> new Ship(
                rs.getString("id"),
                rs.getLong("imo_number"),
                rs.getString("vessel_name"),
                rs.getString("ship_type"),
                rs.getString("flag"),
                rs.getString("homeport"),
                rs.getInt("gross_tonnage"),
                rs.getInt("summer_deadweight_t"),
                rs.getDouble("length_overall_m"),
                rs.getDouble("beam_m"),
                rs.getDouble("draught_m"),
                rs.getInt("year_of_build"),
                rs.getString("builder"),
                rs.getString("place_of_build"),
                rs.getString("yard"),
                rs.getInt("teu"),
                rs.getInt("crude_oil_bbl"),
                rs.getDouble("gas_m3"),
                rs.getString("grain"),
                rs.getString("bale"),
                rs.getString("classification_society"),
                rs.getString("registered_owner"),
                rs.getString("owner_address"),
                rs.getString("owner_email"),
                rs.getString("owner_website"),
                rs.getString("manager"),
                rs.getString("manager_address"),
                rs.getString("manager_email"),
                rs.getString("manager_website")
        );
    }
}
