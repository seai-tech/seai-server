package com.seai.marine.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum VesselType implements AsyncApiEnum {
    BULK_CARRIER("Bulk Carrier"),
    CONTAINER("Container"),
    CRUDE_OIL("Crude Oil"),
    PRODUCT_OIL("Product Oil"),
    LPG("LPG (Liquefied Petroleum Gas)"),
    LNG("LNG (Liquefied Natural Gas)"),
    REEFER("Reefer"),
    RO_RO("Ro-Ro (Roll-On/Roll-Off)"),
    GENERAL_CARGO("General Cargo"),
    CRUISE("Cruise"),
    FERRY("Ferry"),
    OCEAN_LINER("Ocean Liner"),
    CATAMARAN("Catamaran"),
    MOTOR_YACHT("Motor Yacht"),
    SAILING_YACHT("Sailing Yacht"),
    MEGA_YACHT("Mega Yacht"),
    EXPLORER_YACHT("Explorer Yacht"),
    SPORT_FISHING_YACHT("Sport Fishing Yacht");

    @JsonValue
    private final String label;
    private final String name;


    VesselType(String label) {
        this.label = label;
        this.name = name();
    }

    @JsonCreator
    public static VesselType fromName(String name) {
        return Arrays.stream(values())
                .filter(f-> f.name().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
