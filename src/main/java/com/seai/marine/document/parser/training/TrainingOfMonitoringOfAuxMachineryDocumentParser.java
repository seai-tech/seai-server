package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class TrainingOfMonitoringOfAuxMachineryDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingOfMonitoringOfAuxMachineryDocumentParser() {
        super("TRAINING ON MONITORING OF AUXILIARY MACHINERY", "AUX", "MACHINERY");
    }
}