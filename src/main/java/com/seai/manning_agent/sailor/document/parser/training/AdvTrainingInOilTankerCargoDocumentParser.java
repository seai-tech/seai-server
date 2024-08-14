package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentAdvTrainingInOilTankerCargoDocumentParser")
public class AdvTrainingInOilTankerCargoDocumentParser extends AbstractTrainingDocumentParser {

    public AdvTrainingInOilTankerCargoDocumentParser() {
        super("ADVANCED TRAINING IN OIL TANKER CARGO OPERATIONS", "ADV", "TANKER", "CARGO");
    }
}