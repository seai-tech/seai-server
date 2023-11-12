package com.seai.domain.user.model;

import com.seai.domain.voyage.model.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SeaiUser {

    private UUID id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private Rank rank;

    private String presentEmployer;

    private Date dateOfBirth;

    private String manningAgents;

    private Status status;

    private VesselType vesselType;

    private String homeAirport;

    private Date readinessDate;

    private Integer contractDuration;
}
