package com.seai.marine.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class User {

    private UUID id;

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

    private String phone;
}
