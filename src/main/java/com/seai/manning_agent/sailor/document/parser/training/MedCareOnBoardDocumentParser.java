package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentMedCareOnBoardDocumentParser")
public class MedCareOnBoardDocumentParser extends AbstractTrainingDocumentParser {

    public MedCareOnBoardDocumentParser() {
        super("MEDICAL CARE ONBOARD", "MEDICAL", "CARE");
    }
}
