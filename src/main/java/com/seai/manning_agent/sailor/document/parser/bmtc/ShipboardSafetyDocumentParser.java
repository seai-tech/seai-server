package com.seai.manning_agent.sailor.document.parser.bmtc;

import org.springframework.stereotype.Component;

@Component("manningAgentShipboardSafetyDocumentParser")
public class ShipboardSafetyDocumentParser extends AbstractBmtcDocumentParser {

    public ShipboardSafetyDocumentParser() {
        super("SHIPBOARD SAFETY OFFICER COURSE", "SHIPBOARD", "SAFETY");
    }
}
