package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class MedCareOnBoardDocumentParser extends AbstractTrainingDocumentParser {

    public MedCareOnBoardDocumentParser() {
        super("MEDICAL CARE ONBOARD", "MEDICAL", "CARE");
    }
}