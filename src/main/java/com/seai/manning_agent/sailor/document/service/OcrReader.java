package com.seai.manning_agent.sailor.document.service;

import com.seai.spring.config.TrackExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component("manningAgentOcrReader")
@RequiredArgsConstructor
public class OcrReader {

    private final TextractClient amazonTextract;

    @TrackExecution
    public List<String> readDocument(MultipartFile file) throws IOException {
        Document document = Document.builder().bytes(SdkBytes.fromByteArray(file.getBytes())).build();

        DetectDocumentTextRequest analyzeDocumentRequest = DetectDocumentTextRequest.builder().document(document).build();
        DetectDocumentTextResponse detectDocumentTextResult = amazonTextract.detectDocumentText(analyzeDocumentRequest);

        return detectDocumentTextResult.blocks()
                .stream()
                .filter(b-> b.blockType().equals(BlockType.WORD))
                .map(Block::text)
                .collect(Collectors.toList());
    }
}
