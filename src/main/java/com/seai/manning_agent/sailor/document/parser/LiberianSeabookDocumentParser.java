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

@Component("manningAgentLiberianSeabookDocumentParser")
public class LiberianSeabookDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{1,2}-(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)-\\d{2}$");
    private static final Pattern DOCUMENT_NUMBER_PATTERN = Pattern.compile(
            "^\\d{7}$");

    private static final String DOCUMENT_TYPE = "LIBERIAN SEAMAN'S BOOK";

    private final Map<String, Integer> fieldLineMapping;

    public LiberianSeabookDocumentParser() {
        fieldLineMapping = new HashMap<>();
        fieldLineMapping.put("issueDate", 1);
        fieldLineMapping.put("validUntil", 2);
        fieldLineMapping.put("number", 1);
    }

    @Override
    public boolean canParseDocument(List<String> lines) {
        return (lines.stream().anyMatch(l -> l.contains("BOOK:")) && lines.stream().anyMatch(l -> l.contains("FIN:")));
    }

    @Override
    @SneakyThrows
    public MarineDocument parseDocument(List<String> lines) {
        Date issueDate = DATE_FORMATTER.parse(DocumentSeekUtil.findNthMatchForReversed(
                w -> DATE_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("issueDate")));
        Date validUntil = DATE_FORMATTER.parse(DocumentSeekUtil.findNthMatchForReversed(
                w -> DATE_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("validUntil")));
        String number = DocumentSeekUtil.findNthMatchForReversed(
                w -> DOCUMENT_NUMBER_PATTERN.matcher(w).matches(), lines, fieldLineMapping.get("number"));

        return MarineDocument.createNonVerifiedDocument(DOCUMENT_TYPE, number, issueDate, validUntil);
    }
}
