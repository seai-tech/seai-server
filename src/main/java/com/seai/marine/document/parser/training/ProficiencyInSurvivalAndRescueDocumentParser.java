package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class ProficiencyInSurvivalAndRescueDocumentParser extends AbstractTrainingDocumentParser {

    public ProficiencyInSurvivalAndRescueDocumentParser() {
        super("PROFICIENCY IN SURVIVAL CRAFT AND RESCUE BOAT", "SURVIVAL", "RESCUE");
    }
}