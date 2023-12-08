package com.seai.controller;

import com.seai.domain.document.model.MarineDocument;
import com.seai.domain.document.repository.DocumentRepository;
import com.seai.domain.document.service.DocumentFileService;
import com.seai.domain.document.service.DocumentUploadService;
import com.seai.request.VerifyDocumentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
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
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentFileService documentFileService;
    private final DocumentRepository documentRepository;
    private final DocumentUploadService documentUploadService;
    private final DocumentService documentService;

    @PostMapping(value = "/users/{userId}/ocr", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public MarineDocument handleFileUpload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID userId) {
        MarineDocument marineDocument = documentFileService.processFile(multipartFile);
        marineDocument.setPath(String.format("%s/%s/%s", userId.toString(), marineDocument.getName(), marineDocument.getId()));
        documentUploadService.upload(multipartFile, marineDocument.getPath());
        documentRepository.save(marineDocument, userId);
        return marineDocument;
    }

    @PostMapping("/users/{userId}/documents/{documentId}/verify")
    public void verifyDocument(@RequestBody VerifyDocumentRequest verifyDocumentRequest, @PathVariable UUID userId, @PathVariable UUID documentId) {
        documentService.verifyDocument(verifyDocumentRequest, userId, documentId);
    }

    @PostMapping("/users/{userId}/documents/{documentId}/discard")
    public void discard(@PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument document = documentRepository.findDocument(userId, documentId);
        documentUploadService.deleteFile(document.getPath());
    }

    @GetMapping("/users/{userId}/documents")
    public List<MarineDocument> findAllUserDocuments(@PathVariable UUID userId) {
        return documentRepository.findVerifiedByUserId(userId);
    }

    @GetMapping("/users/{userId}/documents/{documentId}")
    public MarineDocument findUserDocument(@PathVariable UUID userId, @PathVariable UUID documentId) {
       return documentRepository.findDocument(userId, documentId);
    }

    @GetMapping("/users/{userId}/documents/{documentId}/download")
    public ResponseEntity<byte[]> download(@PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument document = documentRepository.findVerifiedDocument(userId, documentId);
        byte[] bytes = documentUploadService.downloadDocument(document.getPath());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", "file");
        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.DAYS)
                .noTransform()
                .mustRevalidate();
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .headers(httpHeaders)
                .body(bytes);
    }
}
