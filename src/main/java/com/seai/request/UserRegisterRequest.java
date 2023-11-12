package com.seai.request;

import com.seai.domain.user.model.Status;
import com.seai.domain.user.model.VesselType;
import com.seai.domain.voyage.model.Rank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @NotNull
    @NotEmpty
    private String email;
    @NotNull
    @NotEmpty
    private String password;
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
