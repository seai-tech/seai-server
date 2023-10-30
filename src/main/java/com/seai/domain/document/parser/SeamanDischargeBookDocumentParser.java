package com.seai.domain.document.parser;

import com.seai.domain.document.model.MarineDocument;
import com.seai.domain.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class SeamanDischargeBookDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}\\.\\d{2}\\.\\d{4}$");

    @Override
    public boolean canParseDocument(List<String> lines) {
        return lines.stream().anyMatch(l-> l.contains("SEAMAN")) && lines.stream().anyMatch(l-> l.contains("BOOK"));
    }

    @SneakyThrows
    @Override
    public MarineDocument parseDocument(List<String> lines) {
        String documentType = "SEAMAN'S DISCHARGE BOOK";
        String number = DocumentSeekUtil.findMatchFor(w -> w.matches("^\\d{9}$"), lines, 1);
        String issueDate = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 2);
        String validUntil = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 3);
        return new MarineDocument(documentType, number, DATE_FORMATTER.parse(issueDate), DATE_FORMATTER.parse(validUntil));
    }
}
