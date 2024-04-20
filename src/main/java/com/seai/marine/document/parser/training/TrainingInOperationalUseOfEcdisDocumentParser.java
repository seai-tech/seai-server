package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class TrainingInOperationalUseOfEcdisDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingInOperationalUseOfEcdisDocumentParser() {
        super("TRAINING IN OPERATIONAL USE OF ECDIS AND AIS", "ECDIS", "AIS");
    }
}