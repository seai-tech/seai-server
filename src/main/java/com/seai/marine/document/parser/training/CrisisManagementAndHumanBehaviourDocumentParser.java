package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class CrisisManagementAndHumanBehaviourDocumentParser extends AbstractTrainingDocumentParser {

    public CrisisManagementAndHumanBehaviourDocumentParser() {
        super("CRISIS MANAGEMENT AND HUMAN BEHAVIOUR TRAINING", "CRISIS", "BEHAVIOUR");
    }
}