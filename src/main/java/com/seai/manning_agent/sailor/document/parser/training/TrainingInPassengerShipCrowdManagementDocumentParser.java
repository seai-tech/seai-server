package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentTrainingInPassengerShipCrowdManagementDocumentParser")
public class TrainingInPassengerShipCrowdManagementDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInPassengerShipCrowdManagementDocumentParser() {
        super("TRAINING IN PASSENGER SHIP CROWD MANAGEMENT", "CROWD", "PASSENGER");
    }
}
