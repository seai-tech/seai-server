package com.seai.marine.document.service;

import com.seai.marine.document.model.MarineDocument;
import com.seai.marine.document.parser.DelegatingDocumentParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Document;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentScanner {

    private final TextractClient amazonTextract;
    private final DelegatingDocumentParser delegatingDocumentParser;


    @SneakyThrows
    public MarineDocument readDocument(MultipartFile file) {

        Document document = Document.builder().bytes(SdkBytes.fromByteArray(file.getBytes())).build();

        DetectDocumentTextRequest analyzeDocumentRequest = DetectDocumentTextRequest.builder().document(document).build();
        DetectDocumentTextResponse detectDocumentTextResult = amazonTextract.detectDocumentText(analyzeDocumentRequest);
        List<String> lines = detectDocumentTextResult.blocks()
                .stream()
                .filter(b-> b.blockType().equals(BlockType.WORD))
                .map(Block::text)
                .collect(Collectors.toList());

        return delegatingDocumentParser.parseDocument(lines);
    }
}
