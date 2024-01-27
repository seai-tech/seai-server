package com.seai.document.controller;

import com.seai.document.model.MarineDocument;
import com.seai.document.repository.DocumentRepository;
import com.seai.document.service.DocumentUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FileController {
    private final DocumentRepository documentRepository;
    private final DocumentUploadService documentUploadService;

    @PostMapping("/users/{userId}/documents/{documentId}/files")
    public void upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument document = documentRepository.find(userId, documentId);
        documentUploadService.upload(multipartFile, document.getPath());
    }

    @DeleteMapping("/users/{userId}/documents/{documentId}/files")
    public void delete(@PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument document = documentRepository.find(userId, documentId);
        documentUploadService.delete(document.getPath());
    }

    @GetMapping("/users/{userId}/documents/{documentId}/files")
    public ResponseEntity<byte[]> download(@PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument document = documentRepository.find(userId, documentId);
        byte[] bytes = documentUploadService.download(document.getPath());
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
