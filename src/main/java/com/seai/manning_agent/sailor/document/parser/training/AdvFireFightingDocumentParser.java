package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

@Component("manningAgentAdvFireFightingDocumentParser")
public class AdvFireFightingDocumentParser extends AbstractTrainingDocumentParser {

    public AdvFireFightingDocumentParser() {
        super("ADVANCED FIRE FIGHTING", "ADV", "FIRE");
    }
}
