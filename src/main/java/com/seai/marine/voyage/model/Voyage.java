package com.seai.marine.voyage.model;

import com.seai.marine.user.model.Rank;
import com.seai.marine.user.model.VesselType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voyage {

    private UUID id;

    private String vesselName;

    private VesselType vesselType;

    private Rank rank;

    private String imoNumber;

    private String joiningPort;

    private Date joiningDate;

    private String leavingPort;

    private Date leavingDate;

    private String remarks;
}
