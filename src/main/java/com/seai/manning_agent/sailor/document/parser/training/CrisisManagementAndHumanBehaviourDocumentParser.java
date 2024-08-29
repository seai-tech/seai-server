package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentCrisisManagementAndHumanBehaviourDocumentParser")
public class CrisisManagementAndHumanBehaviourDocumentParser extends AbstractTrainingDocumentParser {

    public CrisisManagementAndHumanBehaviourDocumentParser() {
        super("CRISIS MANAGEMENT AND HUMAN BEHAVIOUR TRAINING", "CRISIS", "BEHAVIOUR");
    }
}
