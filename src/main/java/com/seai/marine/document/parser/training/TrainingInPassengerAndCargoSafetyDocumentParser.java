package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class TrainingInPassengerAndCargoSafetyDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInPassengerAndCargoSafetyDocumentParser() {
        super("TRAINING IN PASSENGER AND CARGO SAFETY AND HULL INTEGRITY", "CARGO", "HULL");
    }
}