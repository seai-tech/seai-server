package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentTrainingInOperationalUseOfEcdisDocumentParser")
public class TrainingInOperationalUseOfEcdisDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInOperationalUseOfEcdisDocumentParser() {
        super("TRAINING IN OPERATIONAL USE OF ECDIS AND AIS", "ECDIS", "AIS");
    }
}