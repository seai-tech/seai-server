package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentTrainingInEngineRoomResourceMgmtDocumentParser")
public class TrainingInEngineRoomResourceMgmtDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInEngineRoomResourceMgmtDocumentParser() {
        super("TRAINING IN ENGINE ROOM RESOURCE MANAGEMENT AND TEAMWORK", "ENGINE", "MANAGEMENT");
    }
}
