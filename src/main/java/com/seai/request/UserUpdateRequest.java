package com.seai.request;

import com.seai.domain.user.model.Status;
import com.seai.domain.user.model.VesselType;
import com.seai.domain.voyage.model.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    private String firstName;

    private String lastName;

    private Rank rank;

    private String presentEmployer;

    private Instant dateOfBirth;

    private String manningAgents;

    private Status status;

    private VesselType vesselType;

    private String homeAirport;

    private Instant readinessDate;

    private Integer contractDuration;
}
