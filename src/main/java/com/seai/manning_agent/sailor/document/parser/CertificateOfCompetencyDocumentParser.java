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

@Component("manningAgentCertificateOfCompetencyDocumentParser")
public class CertificateOfCompetencyDocumentParser implements DocumentParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    private static final String documentType = "CERTIFICATE OF COMPETENCY";
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}$");
    private static final String certificateNumberPattern = "^\\d{7}$";
    private final Map<String, Integer> fieldLineMapping;

    public CertificateOfCompetencyDocumentParser() {
        fieldLineMapping = new HashMap<>();
        fieldLineMapping.put("number", 1);
        fieldLineMapping.put("issueDate", 2);
        fieldLineMapping.put("validUntil", 3);
    }

    @Override
    public boolean canParseDocument(List<String> lines) {
        return lines.stream().anyMatch(l -> l.contains("CERTIFICATE")) && lines.stream().anyMatch(l -> l.contains("COMPETENCY"));
    }

    @SneakyThrows
    @Override
    public MarineDocument parseDocument(List<String> lines) {
        String number = DocumentSeekUtil.findNthMatch(w -> w.matches(certificateNumberPattern), lines, fieldLineMapping.get("number"));
        String issueDate = DocumentSeekUtil.findNthMatch(w -> DATE_PATTERN.matcher(w).matches(), lines, fieldLineMapping.get("issueDate"));
        String validUntil = DocumentSeekUtil.findNthMatch(w -> DATE_PATTERN.matcher(w).matches(), lines, fieldLineMapping.get("validUntil"));

        return MarineDocument.createNonVerifiedDocument(documentType, number, DATE_FORMATTER.parse(issueDate), DATE_FORMATTER.parse(validUntil));
    }
}
