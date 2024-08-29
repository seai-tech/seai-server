package com.seai.manning_agent.sailor.document.parser;

import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Component("manningAgentUsVisaDocumentParser")
public class UsVisaDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private static final Pattern DATE_PATTERN = Pattern.compile("[A-Z]{2}\\d{4}$");
    private static final Pattern DOCUMENT_NUMBER_PATTERN = Pattern.compile("^[A-Z]\\d{7}$");

    private static final String documentType = "US VISA";

    private static final int ISSUE_DATE_INDEX = 2;
    private static final int VALID_UNTIL_INDEX = 3;
    private static final int DOCUMENT_NUMBER_INDEX = 1;
    private static final int DATE_LENGTH = 7;
    private static final int DATE_DAY_END = 2;
    private static final int DATE_MONTH_START = 2;
    private static final int DATE_MONTH_END = 5;
    private static final int DATE_YEAR_START = 5;
    private static final int DATE_YEAR_END = 9;

    private final Map<String, Integer> fieldLineMapping;

    public UsVisaDocumentParser() {
        fieldLineMapping = new HashMap<>();
        fieldLineMapping.put("issueDate", ISSUE_DATE_INDEX);
        fieldLineMapping.put("validUntil", VALID_UNTIL_INDEX);
        fieldLineMapping.put("number", DOCUMENT_NUMBER_INDEX);
    }

    @Override
    public boolean canParseDocument(List<String> lines) {
        return (lines.stream().anyMatch(l -> l.contains("VISA")) && lines.stream().anyMatch(l -> l.contains("USA")))
                || lines.stream().anyMatch(l -> l.contains("VNUSA"));
    }

    @Override
    @SneakyThrows
    public MarineDocument parseDocument(List<String> lines) {
        UsVisaDate issueDate = new UsVisaDate(DocumentSeekUtil.findNthMatch(
                w -> DATE_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("issueDate")));
        UsVisaDate validUntil = new UsVisaDate(DocumentSeekUtil.findNthMatch(
                w -> DATE_PATTERN.matcher(w).find(), lines, fieldLineMapping.get("validUntil")));
        String number = DocumentSeekUtil.findNthMatch(
                w -> DOCUMENT_NUMBER_PATTERN.matcher(w).matches(), reverse(lines), fieldLineMapping.get("number"));

        return MarineDocument.createNonVerifiedDocument(
                documentType,
                number,
                issueDate.asDate(),
                validUntil.asDate());
    }

    private List<String> reverse(List<String> lines) {
        List<String> words = new ArrayList<>(lines);
        Collections.reverse(words);
        return words;
    }

    private static String formatDate(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(DATE_DAY_END, "-");
        sb.insert(DATE_MONTH_END, "-");
        return sb.toString();
    }

    static class UsVisaDate {
        private final String date;
        private final String month;
        private final String year;

        public UsVisaDate(String date) {
            if (date == null || date.length() != DATE_LENGTH) {
                throw new IllegalArgumentException("Invalid date format: " + date);
            }
            this.date = date.substring(0, DATE_DAY_END).replace("O", "0");
            this.month = date.substring(DATE_MONTH_START, DATE_MONTH_END).replace("0", "O");
            this.year = date.substring(DATE_YEAR_START, DATE_YEAR_END);
        }

        @SneakyThrows
        Date asDate() {
            return DATE_FORMATTER.parse(formatDate(date + month + year));
        }
    }
}
