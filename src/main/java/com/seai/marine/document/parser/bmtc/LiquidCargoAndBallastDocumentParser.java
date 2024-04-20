package com.seai.marine.document.parser.bmtc;

import org.springframework.stereotype.Component;

@Component
public class LiquidCargoAndBallastDocumentParser extends AbstractBmtcDocumentParser {

    public LiquidCargoAndBallastDocumentParser() {
        super("LIQUID CARGO AND BALLAST HANDLING ON OIl TANKERS SIMULATOR TRAINING", "BALLAST", "HANDLING");
    }
}
