package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentTrainingOfMonitoringOfAuxMachineryDocumentParser")
public class TrainingOfMonitoringOfAuxMachineryDocumentParser extends AbstractTrainingDocumentParser {

    public TrainingOfMonitoringOfAuxMachineryDocumentParser() {
        super("TRAINING ON MONITORING OF AUXILIARY MACHINERY", "AUX", "MACHINERY");
    }
}
