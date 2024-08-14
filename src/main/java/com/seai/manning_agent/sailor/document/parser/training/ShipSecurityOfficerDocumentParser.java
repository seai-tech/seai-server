package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentShipSecurityOfficerDocumentParser")
public class ShipSecurityOfficerDocumentParser extends AbstractTrainingDocumentParser {

    public ShipSecurityOfficerDocumentParser() {
        super("SHIP SECURITY OFFICER", "SHIP", "SECURITY");
    }
}
