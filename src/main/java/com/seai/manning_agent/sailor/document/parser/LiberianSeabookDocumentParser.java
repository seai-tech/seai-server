package com.seai.manning_agent.sailor.document.parser;

import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Component("manningAgentLiberianSeabookDocumentParser")
public class LiberianSeabookDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{1,2}-(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)-\\d{2}$");

    private static final Pattern DOCUMENT_NUMBER = Pattern.compile(
            "^\\d{7}$");

    @Override
    public boolean canParseDocument(List<String> lines) {
        return (lines.stream().anyMatch(l -> l.contains("BOOK:")) && lines.stream().anyMatch(l -> l.contains("FIN:")));
    }

    @Override
    @SneakyThrows
    public MarineDocument parseDocument(List<String> lines) {
        String documentType = "LIBERIAN SEAMAN'S BOOK";
        Date issueDate = DATE_FORMATTER.parse(DocumentSeekUtil.findMatchForReversed(w -> DATE_PATTERN.matcher(w).find(), lines, 1));
        Date validUntil = DATE_FORMATTER.parse(DocumentSeekUtil.findMatchForReversed(w -> DATE_PATTERN.matcher(w).find(), lines, 2));
        String number = DocumentSeekUtil.findMatchForReversed(w -> DOCUMENT_NUMBER.matcher(w).matches(), lines, 1);
        return MarineDocument.createNonVerifiedDocument(documentType, number, issueDate, validUntil);
    }
}
