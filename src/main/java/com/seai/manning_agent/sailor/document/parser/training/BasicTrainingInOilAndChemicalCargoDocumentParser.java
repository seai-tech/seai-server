package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentBasicTrainingInOilAndChemicalCargoDocumentParser")
public class BasicTrainingInOilAndChemicalCargoDocumentParser extends AbstractTrainingDocumentParser {

    public BasicTrainingInOilAndChemicalCargoDocumentParser() {
        super("BASIC TRAINING IN OIL AND CHEMICAL TANKER CARGO OPERATIONS", "BASIC", "CHEMICAL", "CARGO");
    }
}
