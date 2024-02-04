package com.seai.marine.document.parser;

import com.seai.marine.document.model.MarineDocument;
import com.seai.marine.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class MedicalFirstAidDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public boolean canParseDocument(List<String> lines) {
        return lines.stream().anyMatch(l-> l.contains("MEDICAL")) && lines.stream().anyMatch(l-> l.contains("AID"));
    }

    @SneakyThrows
    @Override
    public MarineDocument parseDocument(List<String> lines) {
        String documentType = "MEDICAL FIRST AID";
        String number = DocumentSeekUtil.findMatchFor(w -> w.matches("^\\d{5}$"), lines, 1);
        String issueDate = DocumentSeekUtil.getWordAfter(w -> w.contains("issue"), lines);
        String validUntil = DocumentSeekUtil.getWordAfter(w -> w.contains("until"), lines);
        return MarineDocument.createNonVerifiedDocument(documentType, number,  DATE_FORMATTER.parse(issueDate), DATE_FORMATTER.parse(validUntil));
    }
}
