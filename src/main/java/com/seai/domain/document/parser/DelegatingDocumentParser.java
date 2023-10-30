package com.seai.domain.document.parser;

import com.seai.domain.document.model.MarineDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DelegatingDocumentParser {

    private final List<DocumentParser> documentParsers;

    public MarineDocument parseDocument(List<String> lines) {
        List<String> words = lines.stream().filter(Objects::nonNull).toList();
        return documentParsers.stream()
                .filter(d -> d.canParseDocument(words))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find parser for document"))
                .parseDocument(words);
    }
}
