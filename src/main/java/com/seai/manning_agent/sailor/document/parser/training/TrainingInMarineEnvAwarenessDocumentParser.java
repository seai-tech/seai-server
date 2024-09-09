package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentTrainingInMarineEnvAwarenessDocumentParser")
public class TrainingInMarineEnvAwarenessDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInMarineEnvAwarenessDocumentParser() {
        super("TRAINING IN MARINE ENVIRONMENTAL AWARENESS", "MARINE", "AWARENESS");
    }
}
