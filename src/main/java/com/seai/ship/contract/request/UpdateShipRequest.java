package com.seai.ship.contract.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateShipRequest {

    @NotBlank
    private String vesselName;

    @NotBlank
    private String shipType;

    @NotBlank
    private String flag;

    private String homeport;

    @NotNull
    @Min(0)
    private Integer grossTonnage;

    @NotNull
    @Min(0)
    private Integer summerDeadweight;

    @NotNull
    @Min(0)
    private Double lengthOverall;

    @NotNull
    @Min(0)
    private Double beam;

    @Min(0)
    private Double draught;

    @NotNull
    private Integer yearOfBuild;

    private String builder;

    private String placeOfBuild;

    private String yard;

    @Min(0)
    private Integer teu;

    @Min(0)
    private Integer crudeOil;

    @Min(0)
    private Double gasCapacity;
    private String grain;

    private String bale;

    private String classificationSociety;

    private String registeredOwner;

    private String ownerAddress;

    @Email
    private String ownerEmail;

    @URL
    private String ownerWebsite;

    private String manager;

    private String managerAddress;

    @Email
    private String managerEmail;

    @URL
    private String managerWebsite;
}
