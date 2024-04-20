package com.seai.marine.document.parser.bmtc;

import org.springframework.stereotype.Component;

@Component
public class ShipboardSafetyDocumentParser extends AbstractBmtcDocumentParser {

    public ShipboardSafetyDocumentParser() {
        super("SHIPBOARD SAFETY OFFICER COURSE", "SHIPBOARD", "SAFETY");
    }
}
