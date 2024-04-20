package com.seai.marine.document.parser.training;

import com.seai.marine.document.model.MarineDocument;
import com.seai.marine.document.parser.DocumentParser;
import com.seai.marine.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

public abstract class AbstractTrainingDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}\\.\\d{2}\\.\\d{4}$");

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
            matches = matches && lines.stream().anyMatch(l-> l.contains(matcher));
        }
        return matches;
    }

    @SneakyThrows
    @Override
    public MarineDocument parseDocument(List<String> lines) {
        String number = DocumentSeekUtil.findMatchFor(w -> w.matches("^\\d{5}$"), lines, 1);
        String issueDate = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 2);
        boolean isUnlimited = lines.contains("Unlimited");
        if (isUnlimited) {
            return MarineDocument.createNonVerifiedDocument(documentType, number,  DATE_FORMATTER.parse(issueDate), null);
        }
        String validUntil = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 3);
        return MarineDocument.createNonVerifiedDocument(documentType, number,  DATE_FORMATTER.parse(issueDate), DATE_FORMATTER.parse(validUntil));
    }
}
