package com.seai.request;

import com.seai.domain.voyage.model.Rank;
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
