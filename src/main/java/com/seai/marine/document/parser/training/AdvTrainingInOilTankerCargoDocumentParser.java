package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class AdvTrainingInOilTankerCargoDocumentParser extends AbstractTrainingDocumentParser {

    public AdvTrainingInOilTankerCargoDocumentParser() {
        super("ADVANCED TRAINING IN OIL TANKER CARGO OPERATIONS", "ADV", "TANKER", "CARGO");
    }
}