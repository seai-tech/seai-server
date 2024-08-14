package com.seai.manning_agent.sailor.document.controller;

import com.seai.manning_agent.sailor.document.service.ManningAgentFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/manning-agents")
@RequiredArgsConstructor
public class ManningAgentFileController {

    private final ManningAgentFileService documentFileService;
    private static final String AUTHORIZATION = "#manningAgentId.equals(authentication.principal.id)";

    @PostMapping("{manningAgentId}/sailors/{sailorId}/documents/{documentId}/files")
    @PreAuthorize(AUTHORIZATION)
    public void upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @PathVariable UUID documentId) {
        documentFileService.upload(multipartFile, manningAgentId, sailorId, documentId);
    }

    @DeleteMapping("{manningAgentId}/sailors/{sailorId}/documents/{documentId}/files")
    @PreAuthorize(AUTHORIZATION)
    public void delete(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @PathVariable UUID documentId) {
        documentFileService.delete(manningAgentId, sailorId, documentId);
    }

    @GetMapping("{manningAgentId}/sailors/{sailorId}/documents/{documentId}/files")
    @PreAuthorize(AUTHORIZATION)
    public ResponseEntity<byte[]> download(@PathVariable UUID manningAgentId, @PathVariable UUID sailorId, @PathVariable UUID documentId) {
        return documentFileService.download(manningAgentId, sailorId, documentId);
    }
}
