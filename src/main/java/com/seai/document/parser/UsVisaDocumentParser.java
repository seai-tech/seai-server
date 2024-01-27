package com.seai.document.parser;

import com.seai.document.model.MarineDocument;
import com.seai.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class UsVisaDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}[A-Z]{3}\\d{4}$");

    private static final Pattern DOCUMENT_NUMBER = Pattern.compile(
            "^\\d{9}$");

    @Override
    public boolean canParseDocument(List<String> lines) {
        return lines.stream().anyMatch(l -> l.contains("VNUSA"));
    }

    @SneakyThrows
    @Override
    public MarineDocument parseDocument(List<String> lines) {
        String documentType = "US VISA";
        String issueDate = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 2);
        String validUntil = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 3);
        String number = DocumentSeekUtil.findMatchFor(w -> DOCUMENT_NUMBER.matcher(w).matches(), lines, 1);
        return MarineDocument.createNonVerifiedDocument(documentType, number, DATE_FORMATTER.parse(formatDate(issueDate)), DATE_FORMATTER.parse(formatDate(validUntil)));
    }

    public String formatDate(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(2, "-");
        sb.insert(6, "-");
        return sb.toString();
    }
}
