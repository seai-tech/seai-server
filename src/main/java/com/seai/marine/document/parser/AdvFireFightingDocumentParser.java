package com.seai.marine.document.parser;

import com.seai.marine.document.model.MarineDocument;
import com.seai.marine.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class AdvFireFightingDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}\\.\\d{2}\\.\\d{4}$");

    @Override
    public boolean canParseDocument(List<String> lines) {
        return lines.stream().anyMatch(l-> l.contains("ADV")) && lines.stream().anyMatch(l-> l.contains("FIRE"));
    }

    @SneakyThrows
    @Override
    public MarineDocument parseDocument(List<String> lines) {
        String documentType = "ADV FIRE FIGHTING";
        String number = DocumentSeekUtil.findMatchFor(w -> w.matches("^\\d{5}$"), lines, 1);
        String issueDate = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 2);
        String validUntil = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 3);
        return MarineDocument.createNonVerifiedDocument(documentType, number, DATE_FORMATTER.parse(issueDate), DATE_FORMATTER.parse(validUntil));
    }
}
