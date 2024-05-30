package com.seai.ship.contract.response;

import lombok.Data;

@Data
public class GetShipByIdResponse {
    private String id;

    private Long imoNumber;

    private String vesselName;

    private String shipType;

    private String flag;

    private String homeport;

    private Integer grossTonnage;

    private Integer summerDeadweight;

    private Double lengthOverall;

    private Double beam;

    private Double draught;

    private Integer yearOfBuild;

    private String builder;

    private String placeOfBuild;

    private String yard;

    private Integer teu;

    private Integer crudeOil;

    private Double gasCapacity;

    private String grain;

    private String bale;

    private String classificationSociety;

    private String registeredOwner;
}
