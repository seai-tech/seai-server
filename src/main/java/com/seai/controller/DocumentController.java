package com.seai.controller;

import com.seai.domain.document.model.MarineDocument;
import com.seai.domain.document.repository.DocumentRepository;
import com.seai.domain.document.service.DocumentService;
import com.seai.domain.document.service.DocumentUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentRepository documentRepository;
    private final DocumentUploadService documentUploadService;

    @PostMapping("/users/{userId}/ocr")
    public MarineDocument handleFileUpload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID userId) {
        MarineDocument marineDocument = documentService.processFile(multipartFile);
        marineDocument.setPath(String.format("%s/%s/%s", userId.toString(), marineDocument.getName(), marineDocument.getId()));
        documentUploadService.upload(multipartFile, marineDocument.getPath());
        documentRepository.save(marineDocument, userId);
        return marineDocument;
    }

    @PostMapping("/users/{userId}/documents/{documentId}/verify")
    public void saveDocument(@RequestBody MarineDocument marineDocument,@PathVariable UUID userId, @PathVariable UUID documentId) {
        documentRepository.verify(marineDocument, userId, documentId);
    }

    @PostMapping("/users/{userId}/documents/{documentId}/discard")
    public void discard(@PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument document = documentRepository.findVerifiedDocument(userId, documentId);
        documentUploadService.deleteFile(document.getPath());
    }

    @GetMapping("/users/{userId}/documents")
    public List<MarineDocument> saveDocument(@PathVariable UUID userId) {
        return documentRepository.findVerifiedByUserId(userId);
    }

    @GetMapping("/users/{userId}/documents/{documentId}/download")
    public ResponseEntity<byte[]> download(@PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument document = documentRepository.findVerifiedDocument(userId, documentId);
        byte[] bytes = documentUploadService.downloadDocument(document.getPath());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", "file");

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }
}
