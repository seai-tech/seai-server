package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentTrainingInPassengerAndCargoSafetyDocumentParser")
public class TrainingInPassengerAndCargoSafetyDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInPassengerAndCargoSafetyDocumentParser() {
        super("TRAINING IN PASSENGER AND CARGO SAFETY AND HULL INTEGRITY", "CARGO", "HULL");
    }
}
