package com.seai.marine.document.parser;

import com.seai.marine.document.model.MarineDocument;
import com.seai.marine.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class UsVisaDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}[A-Z]{3}\\d{4}$");
    private static final Pattern DATE_PATTERN_2 = Pattern.compile(
            "[A-Z]{2}\\d{4}$");

    private static final Pattern DOCUMENT_NUMBER = Pattern.compile(
            "^[A-Z]\\d{7}$");

    @Override
    public boolean canParseDocument(List<String> lines) {
        return (lines.stream().anyMatch(l -> l.contains("VISA")) && lines.stream().anyMatch(l -> l.contains("USA")))
                || lines.stream().anyMatch(l -> l.contains("VNUSA"));
    }

    @Override
    public MarineDocument parseDocument(List<String> lines) {
        String documentType = "US VISA";
        UsVisaDate issueDate = new UsVisaDate(DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN_2.matcher(w).find(), lines, 2));
        UsVisaDate validUntil = new UsVisaDate(DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN_2.matcher(w).find(), lines, 3));
        String number = DocumentSeekUtil.findMatchFor(w -> DOCUMENT_NUMBER.matcher(w).matches(), reverse(lines), 1);
        return MarineDocument.createNonVerifiedDocument(documentType, number, issueDate.asDate(), validUntil.asDate());
    }

    //Used to reverse and get first occurrence as we can have multiple matches for doc number regex
    private List<String> reverse(List<String> lines) {
        ArrayList<String> words = new ArrayList<>(lines);
        Collections.reverse(words);
        return words;
    }

    private static String formatDate(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(2, "-");
        sb.insert(6, "-");
        return sb.toString();
    }

    static class UsVisaDate {
        String date;

        String month;

        String year;

        public UsVisaDate(String date) {
            this.date = date.substring(0, 2).replaceAll("O", "0");
            this.month = date.substring(2, 5).replaceAll("0", "O");
            this.year = date.substring(5, 9);
        }

        @SneakyThrows
        Date asDate() {
            return DATE_FORMATTER.parse(formatDate(date + month + year));
        }
    }
}
