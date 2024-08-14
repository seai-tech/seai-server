package com.seai.manning_agent.sailor.document.parser.training;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component("manningAgentUsBasicSafetyTrainingDocumentParser")
public class BasicSafetyTrainingDocumentParser extends AbstractTrainingDocumentParser {
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}\\.\\d{2}\\.\\d{4}$");

    public BasicSafetyTrainingDocumentParser() {
        super("BASIC SAFETY TRAINING", "BASIC", "SAFETY", "TRAINING");
    }
}
