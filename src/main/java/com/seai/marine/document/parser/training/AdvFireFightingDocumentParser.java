package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class AdvFireFightingDocumentParser extends AbstractTrainingDocumentParser {

    public AdvFireFightingDocumentParser() {
        super("ADVANCED FIRE FIGHTING", "ADV", "FIRE");
    }
}