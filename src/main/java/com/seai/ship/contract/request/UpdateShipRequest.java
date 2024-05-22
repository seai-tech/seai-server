package com.seai.ship.contract.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateShipRequest {

    private String vesselName;

    private String shipType;

    private String flag;

    private String homeport;

    private Integer grossTonnage;

    private Integer summerDeadweight;

    private Double lengthOverall;

    private Double beam;

    private Double draught;

    private String yard;

    private Integer teu;

    private Integer crudeOil;

    private Double gasCapacity;

    private String grain;

    private String bale;

    private String classificationSociety;

    private String registeredOwner;

    private String ownerAddress;

    private String ownerEmail;

    private String ownerWebsite;

    private String manager;

    private String managerAddress;

    private String managerEmail;

    private String managerWebsite;
}
