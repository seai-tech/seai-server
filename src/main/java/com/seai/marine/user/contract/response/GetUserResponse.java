package com.seai.marine.user.contract.response;

import com.seai.marine.user.model.Nationality;
import com.seai.marine.user.model.Rank;
import com.seai.marine.user.model.Status;
import com.seai.marine.user.model.VesselType;
import com.seai.marine.next_of_kin.model.NextOfKin;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GetUserResponse {

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

    private String address;

    private Nationality nationality;

    private NextOfKin nextOfKin;
}

