package com.seai.manning_agent.sailor.document.parser;

import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.parser.util.DocumentSeekUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

@Component("manningAgentCertificateOfCompetencyDocumentParser")
public class CertificateOfCompetencyDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}\\.\\d{2}\\.\\d{4}$");

    @Override
    public boolean canParseDocument(List<String> lines) {
        return lines.stream().anyMatch(l-> l.contains("CERTIFICATE")) && lines.stream().anyMatch(l-> l.contains("COMPETENCY"));
    }

    @SneakyThrows
    @Override
    public MarineDocument parseDocument(List<String> lines) {
        String documentType = "CERTIFICATE OF COMPETENCY";
        String number = DocumentSeekUtil.findMatchFor(w -> w.matches("^\\d{7}$"), lines, 1);
        String issueDate = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 2);
        String validUntil = DocumentSeekUtil.findMatchFor(w -> DATE_PATTERN.matcher(w).matches(), lines, 3);
        return MarineDocument.createNonVerifiedDocument(documentType, number, DATE_FORMATTER.parse(issueDate), DATE_FORMATTER.parse(validUntil));
    }
}
