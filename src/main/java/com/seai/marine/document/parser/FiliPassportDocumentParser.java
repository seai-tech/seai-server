package com.seai.marine.document.parser;

import com.seai.marine.document.model.MarineDocument;
import com.seai.marine.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class FiliPassportDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private static final Pattern YAER_PATTERN = Pattern.compile(
            "^\\d{4}$");
    private static final Pattern MONTH_PATTERN = Pattern.compile(
            "^(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)$");
    private static final Pattern DAY_PATTERN = Pattern.compile(
            "^\\d{1,2}$");

    private static final Pattern DOCUMENT_NUMBER = Pattern.compile(
            "^P\\d{7}[A-Z]$");

    @Override
    public boolean canParseDocument(List<String> lines) {
        return lines.stream().anyMatch(l -> l.contains("PILIPINAS"));
    }

    @Override
    @SneakyThrows
    public MarineDocument parseDocument(List<String> lines) {
        String documentType = "PHILIPPINE PASSPORT";
        String issueDay = DocumentSeekUtil.findMatchForReversed(w -> DAY_PATTERN.matcher(w).find(), lines, 2);
        String issueMonth = DocumentSeekUtil.findMatchForReversed(w -> MONTH_PATTERN.matcher(w).find(), lines, 2);
        String issueYear = DocumentSeekUtil.findMatchForReversed(w -> YAER_PATTERN.matcher(w).find(), lines, 2);

        String expiryDay = DocumentSeekUtil.findMatchForReversed(w -> DAY_PATTERN.matcher(w).find(), lines, 1);
        String expiryMonth = DocumentSeekUtil.findMatchForReversed(w -> MONTH_PATTERN.matcher(w).find(), lines, 1);
        String expiryYear = DocumentSeekUtil.findMatchForReversed(w -> YAER_PATTERN.matcher(w).find(), lines, 1);


        String number = DocumentSeekUtil.findMatchFor(w -> DOCUMENT_NUMBER.matcher(w).matches(), lines, 1);
        return MarineDocument.createNonVerifiedDocument(documentType, number, toDate(issueDay, issueMonth, issueYear), toDate(expiryDay, expiryMonth, expiryYear));
    }

    @SneakyThrows
    private static Date toDate(String day, String month, String year) {
        return DATE_FORMATTER.parse(String.format("%s-%s-%s", day, month, year));
    }
}
