package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentTrainingInBridgeResourceMgmtDocumentParser")
public class TrainingInBridgeResourceMgmtDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInBridgeResourceMgmtDocumentParser() {
        super("TRAINING IN BRIDGE RESOURCE MANAGEMENT AND TEAMWORK", "BRIDGE", "MANAGEMENT");
    }
}
