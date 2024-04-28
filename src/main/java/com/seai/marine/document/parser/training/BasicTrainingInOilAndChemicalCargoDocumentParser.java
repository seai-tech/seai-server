package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class BasicTrainingInOilAndChemicalCargoDocumentParser extends AbstractTrainingDocumentParser {

    public BasicTrainingInOilAndChemicalCargoDocumentParser() {
        super("BASIC TRAINING IN OIL AND CHEMICAL TANKER CARGO OPERATIONS", "BASIC", "CHEMICAL", "CARGO");
    }
}