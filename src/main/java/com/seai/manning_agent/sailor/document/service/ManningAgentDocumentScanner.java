package com.seai.manning_agent.sailor.document.service;

import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.parser.DelegatingDocumentParser;
import com.seai.spring.config.TrackExecution;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManningAgentDocumentScanner {
    @Autowired
    @Qualifier("manningAgentDelegatingDocumentParser")
    private final DelegatingDocumentParser delegatingDocumentParser;

    @Autowired
    @Qualifier("manningAgentOcrReader")
    private  final OcrReader documentReader;


    @SneakyThrows
    @TrackExecution
    public MarineDocument readDocument(MultipartFile file) {
        List<String> lines = documentReader.readDocument(file);

        return delegatingDocumentParser.parseDocument(lines);
    }
}
