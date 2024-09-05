package com.seai.marine.document.parser;

import com.seai.common.exception.ReadDocumentException;
import com.seai.marine.document.model.MarineDocument;
import com.seai.spring.config.TrackExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DelegatingDocumentParser {

    private final List<DocumentParser> documentParsers;

    @TrackExecution
    public MarineDocument parseDocument(List<String> lines) {
        List<String> words = lines.stream().filter(Objects::nonNull).toList();
        return documentParsers.stream()
                .filter(d -> d.canParseDocument(words))
                .findFirst()
                .orElseThrow(() -> new ReadDocumentException("Could not recognize document"))
                .parseDocument(words);
    }
}
