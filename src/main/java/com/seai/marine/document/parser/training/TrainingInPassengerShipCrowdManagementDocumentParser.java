package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class TrainingInPassengerShipCrowdManagementDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInPassengerShipCrowdManagementDocumentParser() {
        super("TRAINING IN PASSENGER SHIP CROWD MANAGEMENT", "CROWD", "PASSENGER");
    }
}