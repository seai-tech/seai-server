package com.seai.manning_agent.sailor.document.parser;

import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component("manningAgentPassportDocumentParser")
public class PassportDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}\\.\\d{2}\\.\\d{4}$");
    private static final Pattern DOCUMENT_NUMBER_PATTERN = Pattern.compile(
            "^\\d{9}$");

    private static final String DOCUMENT_TYPE = "PASSPORT";

    private final Map<String, Integer> fieldLineMapping;

    public PassportDocumentParser() {
        fieldLineMapping = new HashMap<>();
        fieldLineMapping.put("number", 1);
        fieldLineMapping.put("issueDate", 2);
        fieldLineMapping.put("validUntil", 3);
    }

    @Override
    public boolean canParseDocument(List<String> lines) {
        return lines.stream().anyMatch(l -> l.contains("PASSPORT")) && lines.stream().noneMatch(l -> l.contains("SEAMAN"));
    }

    @Override
    @SneakyThrows
    public MarineDocument parseDocument(List<String> lines) {
        String number = DocumentSeekUtil.findNthMatch(
                w -> DOCUMENT_NUMBER_PATTERN.matcher(w).matches(), lines, fieldLineMapping.get("number"));
        String issueDate = DocumentSeekUtil.findNthMatch(
                w -> DATE_PATTERN.matcher(w).matches(), lines, fieldLineMapping.get("issueDate"));
        String validUntil = DocumentSeekUtil.findNthMatch(
                w -> DATE_PATTERN.matcher(w).matches(), lines, fieldLineMapping.get("validUntil"));

        return MarineDocument.createNonVerifiedDocument(
                DOCUMENT_TYPE,
                number,
                DATE_FORMATTER.parse(issueDate),
                DATE_FORMATTER.parse(validUntil));
    }
}
