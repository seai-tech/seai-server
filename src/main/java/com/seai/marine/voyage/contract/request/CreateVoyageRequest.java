package com.seai.marine.voyage.contract.request;

import com.seai.marine.user.model.Rank;
import com.seai.marine.user.model.VesselType;
import lombok.Data;

import java.time.Instant;

@Data
public class CreateVoyageRequest {

    private String vesselName;

    private VesselType vesselType;

    private Rank rank;

    private String imoNumber;

    private String joiningPort;

    private Instant joiningDate;

    private String leavingPort;

    private Instant leavingDate;

    private String remarks;

    private String flag;
}
