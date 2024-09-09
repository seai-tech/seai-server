package com.seai.manning_agent.sailor.document.parser.bmtc;

import org.springframework.stereotype.Component;

@Component("manningAgentLiquidCargoAndBallastDocumentParser")
public class LiquidCargoAndBallastDocumentParser extends AbstractBmtcDocumentParser {

    public LiquidCargoAndBallastDocumentParser() {
        super("LIQUID CARGO AND BALLAST HANDLING ON OIL TANKERS SIMULATOR TRAINING", "BALLAST", "HANDLING");
    }
}
