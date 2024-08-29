package com.seai.manning_agent.sailor.document.parser.training;

import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.parser.DocumentParser;
import com.seai.manning_agent.sailor.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

public abstract class AbstractTrainingDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d{5}$");

    private static final int NUMBER_MATCH_INDEX = 1;
    private static final int ISSUE_DATE_MATCH_INDEX = 2;
    private static final int VALID_UNTIL_DATE_MATCH_INDEX = 3;

    private final String documentType;
    private final String[] matchers;

    public AbstractTrainingDocumentParser(String documentType, String... matchers) {
        this.documentType = documentType;
        this.matchers = matchers;
    }

    @Override
    public boolean canParseDocument(List<String> lines) {
        boolean matches = true;
        for (String matcher : matchers) {
            matches = matches && lines.stream().anyMatch(l -> l.contains(matcher));
        }
        return matches;
    }

    @SneakyThrows
    @Override
    public MarineDocument parseDocument(List<String> lines) {
        String number = DocumentSeekUtil.findNthMatch(w -> NUMBER_PATTERN.matcher(w).matches(), lines, NUMBER_MATCH_INDEX);
        String issueDateString = DocumentSeekUtil.findNthMatch(w -> DATE_PATTERN.matcher(w).matches(), lines, ISSUE_DATE_MATCH_INDEX);

        boolean isUnlimited = lines.contains("Unlimited");
        if (isUnlimited) {
            return MarineDocument.createNonVerifiedDocument(documentType, number, DATE_FORMATTER.parse(issueDateString), null);
        } else {
            String validUntilDateString = DocumentSeekUtil.findNthMatch(w -> DATE_PATTERN.matcher(w).matches(), lines, VALID_UNTIL_DATE_MATCH_INDEX);
            return MarineDocument.createNonVerifiedDocument(documentType, number, DATE_FORMATTER.parse(issueDateString), DATE_FORMATTER.parse(validUntilDateString));
        }
    }
}
