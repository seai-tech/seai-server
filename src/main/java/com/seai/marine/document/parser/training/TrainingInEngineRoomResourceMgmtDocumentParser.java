package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class TrainingInEngineRoomResourceMgmtDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInEngineRoomResourceMgmtDocumentParser() {
        super("TRAINING IN ENGINE ROOM RESOURCE MANAGEMENT AND TEAMWORK", "ENGINE", "MANAGEMENT");
    }
}