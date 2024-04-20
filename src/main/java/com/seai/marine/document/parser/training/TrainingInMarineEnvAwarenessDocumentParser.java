package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class TrainingInMarineEnvAwarenessDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInMarineEnvAwarenessDocumentParser() {
        super("TRAINING IN MARINE ENVIRONMENTAL AWARENESS", "MARINE", "AWARENESS");
    }
}