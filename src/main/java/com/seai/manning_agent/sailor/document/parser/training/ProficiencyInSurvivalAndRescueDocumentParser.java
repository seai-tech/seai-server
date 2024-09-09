package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentProficiencyInSurvivalAndRescueDocumentParser")
public class ProficiencyInSurvivalAndRescueDocumentParser extends AbstractTrainingDocumentParser {

    public ProficiencyInSurvivalAndRescueDocumentParser() {
        super("PROFICIENCY IN SURVIVAL CRAFT AND RESCUE BOAT", "SURVIVAL", "RESCUE");
    }
}
