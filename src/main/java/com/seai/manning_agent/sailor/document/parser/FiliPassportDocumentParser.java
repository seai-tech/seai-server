package com.seai.manning_agent.sailor.document.parser;

import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@Component("manningAgentFilePassportDocumentParser")
public class FiliPassportDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private static final Pattern YEAR_PATTERN = Pattern.compile("^\\d{4}$");
    private static final Pattern MONTH_PATTERN = Pattern.compile("^(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)$");
    private static final Pattern DAY_PATTERN = Pattern.compile("^\\d{1,2}$");
    private static final Pattern DOCUMENT_NUMBER_PATTERN = Pattern.compile("^P\\d{7}[A-Z]$");

    private static final String DOCUMENT_TYPE = "PHILIPPINE PASSPORT";

    private final Map<String, Integer> fieldLineMapping;

    public FiliPassportDocumentParser() {
        fieldLineMapping = new HashMap<>();
        fieldLineMapping.put("issueDay", 2);
        fieldLineMapping.put("issueMonth", 2);
        fieldLineMapping.put("issueYear", 2);
        fieldLineMapping.put("expiryDay", 1);
        fieldLineMapping.put("expiryMonth", 1);
        fieldLineMapping.put("expiryYear", 1);
        fieldLineMapping.put("number", 1);
    }

    @Override
    public boolean canParseDocument(List<String> lines) {
        return lines.stream().anyMatch(l -> l.contains("PILIPINAS"));
    }

    @Override
    @SneakyThrows
    public MarineDocument parseDocument(List<String> lines) {
        String issueDay = DocumentSeekUtil.findNthMatchForReversed(
                w -> DAY_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("issueDay"));
        String issueMonth = DocumentSeekUtil.findNthMatchForReversed(
                w -> MONTH_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("issueMonth"));
        String issueYear = DocumentSeekUtil.findNthMatchForReversed(
                w -> YEAR_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("issueYear"));

        String expiryDay = DocumentSeekUtil.findNthMatchForReversed(
                w -> DAY_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("expiryDay"));
        String expiryMonth = DocumentSeekUtil.findNthMatchForReversed(
                w -> MONTH_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("expiryMonth"));
        String expiryYear = DocumentSeekUtil.findNthMatchForReversed(
                w -> YEAR_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("expiryYear"));

        String number = DocumentSeekUtil.findNthMatch(
                w -> DOCUMENT_NUMBER_PATTERN.matcher(w).matches(), lines, fieldLineMapping.get("number"));

        return MarineDocument.createNonVerifiedDocument(
                DOCUMENT_TYPE,
                number,
                toDate(issueDay, issueMonth, issueYear),
                toDate(expiryDay, expiryMonth, expiryYear)
        );
    }

    @SneakyThrows
    private static Date toDate(String day, String month, String year) {
        return DATE_FORMATTER.parse(String.format("%s-%s-%s", day, month, year));
    }
}
