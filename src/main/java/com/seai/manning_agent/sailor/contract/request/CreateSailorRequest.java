package com.seai.manning_agent.sailor.contract.request;

import com.seai.marine.user.model.Rank;
import com.seai.marine.user.model.Status;
import com.seai.marine.user.model.VesselType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSailorRequest {

    private String firstName;

    private String lastName;

    private Rank rank;

    private String presentEmployer;

    private Instant dateOfBirth;

    private Status status;

    private VesselType vesselType;

    private String homeAirport;

    private Instant readinessDate;

    private Integer contractDuration;

    private String phone;
}
