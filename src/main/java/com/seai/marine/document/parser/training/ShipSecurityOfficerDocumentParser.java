package com.seai.marine.document.parser.training;

import org.springframework.stereotype.Component;

@Component
public class ShipSecurityOfficerDocumentParser extends AbstractTrainingDocumentParser {

    public ShipSecurityOfficerDocumentParser() {
        super("SHIP SECURITY OFFICER", "SHIP", "SECURITY");
    }
}
