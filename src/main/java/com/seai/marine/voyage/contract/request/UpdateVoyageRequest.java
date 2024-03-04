package com.seai.marine.voyage.contract.request;

import com.seai.marine.user.model.Rank;
import lombok.Data;

import java.time.Instant;


@Data
public class UpdateVoyageRequest {

    private String vesselName;

    private Rank rank;

    private String imoNumber;

    private String joiningPort;

    private Instant joiningDate;

    private String leavingPort;

    private Instant leavingDate;

    private String remarks;
}
