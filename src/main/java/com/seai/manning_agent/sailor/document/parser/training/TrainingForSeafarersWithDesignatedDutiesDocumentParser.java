package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentTrainingForSeafarersWithDesignatedDutiesDocumentParser")
public class TrainingForSeafarersWithDesignatedDutiesDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingForSeafarersWithDesignatedDutiesDocumentParser() {
        super("TRAINING FOR SEAFARERS WITH DESIGNATED SECURITY DUTIES", "DESIGNATED", "SEAFARERS", "DUTIES");
    }
}