package com.seai.marine.voyage.contract.response;

import com.seai.marine.voyage.model.Rank;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class GetVoyageResponse {

    private UUID id;

    private String vesselName;

    private Rank rank;

    private String imoNumber;

    private String joiningPort;

    private Date joiningDate;

    private String leavingPort;

    private Date leavingDate;

    private String remarks;
}
