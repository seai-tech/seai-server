package com.seai.domain.document.service;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DetectDocumentTextRequest;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.Document;
import com.seai.domain.document.model.MarineDocument;
import com.seai.domain.document.parser.DelegatingDocumentParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentFileService {

    private final AmazonTextract amazonTextract;
    private final DelegatingDocumentParser delegatingDocumentParser;

    @SneakyThrows
    public MarineDocument processFile(MultipartFile file) {

        Document myDoc = new Document().withBytes(ByteBuffer.wrap(file.getInputStream().readAllBytes()));

        DetectDocumentTextRequest analyzeDocumentRequest = new DetectDocumentTextRequest()
                .withDocument(myDoc);
        DetectDocumentTextResult detectDocumentTextResult = amazonTextract.detectDocumentText(analyzeDocumentRequest);
        List<String> lines = detectDocumentTextResult.getBlocks()
                .stream()
                .filter(b-> b.getBlockType().equals("WORD"))
                .map(Block::getText)
                .collect(Collectors.toList());

        return delegatingDocumentParser.parseDocument(lines);
    }
}
