package com.seai.voyage.contract.request;

import com.seai.voyage.model.Rank;
import lombok.Data;

import java.time.Instant;

@Data
public class CreateVoyageRequest {

    private String vesselName;

    private Rank rank;

    private String imoNumber;

    private String joiningPort;

    private Instant joiningDate;

    private String leavingPort;

    private Instant leavingDate;

    private String remarks;
}
