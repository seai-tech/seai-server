package com.seai.marine.document.service;

import com.seai.marine.document.model.MarineDocument;
import com.seai.marine.document.parser.DelegatingDocumentParser;
import com.seai.spring.config.TrackExecution;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentScanner {

    private final DelegatingDocumentParser delegatingDocumentParser;
    private final OcrReader documentReader;


    @SneakyThrows
    @TrackExecution
    public MarineDocument readDocument(MultipartFile file) {
        List<String> lines = documentReader.readDocument(file);

        return delegatingDocumentParser.parseDocument(lines);
    }
}
