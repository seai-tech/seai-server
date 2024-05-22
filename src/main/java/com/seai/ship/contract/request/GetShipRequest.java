package com.seai.ship.contract.request;

import lombok.Data;

@Data
public class GetShipRequest {
    private String vesselName;

    private String owner;

    private String rank;

}
