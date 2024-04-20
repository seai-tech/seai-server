package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class TrainingInBridgeResourceMgmtDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInBridgeResourceMgmtDocumentParser() {
        super("TRAINING IN BRIDGE RESOURCE MANAGEMENT AND TEAMWORK", "BRIDGE", "MANAGEMENT");
    }
}