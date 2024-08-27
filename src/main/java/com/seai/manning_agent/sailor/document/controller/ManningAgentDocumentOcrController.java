package com.seai.manning_agent.sailor.document.controller;

import com.seai.manning_agent.sailor.document.contract.response.GetDocumentResponse;
import com.seai.manning_agent.sailor.document.service.ManningAgentDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ManningAgentDocumentOcrController {
    private final ManningAgentDocumentService documentService;
    private static final String AUTHORIZATION = "#manningAgentId.equals(authentication.principal.id)";

    //OCR
    @PostMapping(value = "{manningAgentId}/sailors/{sailorId}/ocr", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize(AUTHORIZATION)
    public GetDocumentResponse upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID manningAgentId, @PathVariable UUID sailorId) {
        return documentService.upload(multipartFile, manningAgentId, sailorId);
    }
}
