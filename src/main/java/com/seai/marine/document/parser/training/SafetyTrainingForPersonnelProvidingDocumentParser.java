package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class SafetyTrainingForPersonnelProvidingDocumentParser extends AbstractTrainingDocumentParser {

    public SafetyTrainingForPersonnelProvidingDocumentParser() {
        super("SAFETY TRAINING FOR PERSONNEL PROVIDING DIRECT SERVICES TO PASSENGERS IN PASSENGER SPACES", "SPACES", "PASSENGER");
    }
}