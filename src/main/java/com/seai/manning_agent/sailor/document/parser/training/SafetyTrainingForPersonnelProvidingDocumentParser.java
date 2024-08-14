package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentSafetyTrainingForPersonnelProvidingDocumentParser")
public class SafetyTrainingForPersonnelProvidingDocumentParser extends AbstractTrainingDocumentParser {

    public SafetyTrainingForPersonnelProvidingDocumentParser() {
        super("SAFETY TRAINING FOR PERSONNEL PROVIDING DIRECT SERVICES TO PASSENGERS IN PASSENGER SPACES", "SPACES", "PASSENGER");
    }
}