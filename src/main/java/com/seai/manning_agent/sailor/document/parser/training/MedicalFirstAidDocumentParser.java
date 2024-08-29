package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentMedicalFirstAidDocumentParser")
public class MedicalFirstAidDocumentParser extends AbstractTrainingDocumentParser {

    public MedicalFirstAidDocumentParser() {
        super("MEDICAL FIRST AID", "MEDICAL", "AID");
    }
}

