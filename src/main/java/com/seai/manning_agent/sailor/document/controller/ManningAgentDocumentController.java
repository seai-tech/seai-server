package com.seai.manning_agent.sailor.document.controller;

import com.seai.manning_agent.sailor.document.contract.request.CreateDocumentRequest;
import com.seai.manning_agent.sailor.document.contract.request.UpdateDocumentRequest;
import com.seai.manning_agent.sailor.document.contract.response.CreateDocumentResponse;
import com.seai.manning_agent.sailor.document.contract.response.GetDocumentResponse;
import com.seai.manning_agent.sailor.document.service.ManningAgentDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/manning-agents")
@RequiredArgsConstructor
public class ManningAgentDocumentController {

    private final ManningAgentDocumentService documentService;
    private static final String AUTHORIZATION = "#manningAgentId.equals(authentication.principal.id)";

    //OCR
    @PostMapping(value = "{manningAgentId}/sailors/{sailorId}/ocr", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize(AUTHORIZATION)
    public GetDocumentResponse upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID manningAgentId, @PathVariable UUID sailorId) {
        return documentService.upload(multipartFile, manningAgentId, sailorId);
    }

    //CREATE
    @PostMapping("{manningAgentId}/sailors/{sailorId}/documents")
    @PreAuthorize(AUTHORIZATION)
    public CreateDocumentResponse create(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @RequestBody CreateDocumentRequest createDocumentRequest) {
        return documentService.create(manningAgentId, sailorId, createDocumentRequest);
    }

    //READ
    @GetMapping("{manningAgentId}/sailors/{sailorId}/documents/{documentId}")
    @PreAuthorize(AUTHORIZATION)
    public GetDocumentResponse find(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @PathVariable UUID documentId) {
        return documentService.find(manningAgentId, sailorId, documentId);
    }

    //UPDATE
    @PutMapping("{manningAgentId}/sailors/{sailorId}/documents/{documentId}")
    @PreAuthorize(AUTHORIZATION)
    public void update(@RequestBody UpdateDocumentRequest updateDocumentRequest, @PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @PathVariable UUID documentId) {
        documentService.update(updateDocumentRequest, manningAgentId, sailorId, documentId);
    }

    //DELETE
    @DeleteMapping("{manningAgentId}/sailors/{sailorId}/documents/{documentId}")
    @PreAuthorize(AUTHORIZATION)
    public void delete(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @PathVariable UUID documentId) {
        documentService.delete(manningAgentId, sailorId, documentId);
    }

    //FIND ALL
    @GetMapping("{manningAgentId}/sailors/{sailorId}/documents")
    @PreAuthorize(AUTHORIZATION)
    public List<GetDocumentResponse> findAll(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId) {
        return documentService.findAll(manningAgentId, sailorId);
    }
}
