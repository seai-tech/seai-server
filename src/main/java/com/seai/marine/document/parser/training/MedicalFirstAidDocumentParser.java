package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class MedicalFirstAidDocumentParser extends AbstractTrainingDocumentParser {

    public MedicalFirstAidDocumentParser() {
        super("MEDICAL FIRST AID", "MEDICAL", "AID");
    }
}
