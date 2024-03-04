package com.seai.marine.user.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum VesselType {
    BULK_CARRIER("Bulk Carrier"),
    CONTAINER("Container"),
    CRUDE_OIL("Crude Oil"),
    PRODUCT_OIL("Product Oil"),
    LPG("LPG (Liquefied Petroleum Gas)"),
    LNG("LNG (Liquefied Natural Gas"),
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

    VesselType(String label) {
        this.label = label;
    }
}
