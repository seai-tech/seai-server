package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class TrainingForSeafarersWithDesignatedDutiesDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingForSeafarersWithDesignatedDutiesDocumentParser() {
        super("TRAINING FOR SEAFARERS WITH DESIGNATED SECURITY DUTIES", "DESIGNATED", "SEAFARERS", "DUTIES");
    }
}