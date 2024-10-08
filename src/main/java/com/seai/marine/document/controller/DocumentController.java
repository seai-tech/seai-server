package com.seai.marine.document.controller;

import com.seai.marine.document.contract.request.CreateDocumentRequest;
import com.seai.marine.document.contract.request.UpdateDocumentRequest;
import com.seai.marine.document.contract.response.CreateDocumentResponse;
import com.seai.marine.document.contract.response.GetDocumentResponse;
import com.seai.marine.document.mapper.DocumentMapper;
import com.seai.marine.document.model.MarineDocument;
import com.seai.marine.document.repository.DocumentRepository;
import com.seai.marine.document.service.DocumentFileService;
import com.seai.marine.document.service.DocumentScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    private final DocumentScanner documentScanner;
    private final DocumentRepository documentRepository;
    private final DocumentFileService documentFileService;
    private final DocumentMapper documentMapper;

    @PostMapping(value = "/users/{userId}/ocr", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public GetDocumentResponse upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID userId) {
        MarineDocument marineDocument = documentScanner.readDocument(multipartFile);
        documentRepository.save(marineDocument, userId);
        documentFileService.upload(multipartFile, marineDocument.getPath());
        return documentMapper.map(marineDocument);
    }

    @PostMapping("/users/{userId}/documents")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public CreateDocumentResponse create(@PathVariable UUID userId, @RequestBody CreateDocumentRequest createDocumentRequest) {
        MarineDocument verifiedDocument = MarineDocument.createVerifiedDocument(createDocumentRequest.getName(),
                createDocumentRequest.getNumber(),
                createDocumentRequest.getIssueDate(),
                createDocumentRequest.getExpiryDate());
        return documentMapper.mapCreate(documentRepository.save(verifiedDocument, userId));
    }

    @GetMapping("/users/{userId}/documents/{documentId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public GetDocumentResponse find(@PathVariable UUID userId, @PathVariable UUID documentId) {
        return documentMapper.map(documentRepository.find(userId, documentId));
    }

    @PutMapping("/users/{userId}/documents/{documentId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void update(@RequestBody UpdateDocumentRequest updateDocumentRequest, @PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument marineDocument = documentMapper.map(updateDocumentRequest);
        documentRepository.update(marineDocument, userId, documentId);
    }

    @DeleteMapping("/users/{userId}/documents/{documentId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void delete(@PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument document = documentRepository.find(userId, documentId);
        documentFileService.delete(document.getPath());
        documentRepository.delete(documentId, userId);
    }

    @GetMapping("/users/{userId}/documents")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public List<GetDocumentResponse> findAll(@PathVariable UUID userId) {
        return documentRepository.findAll(userId).stream().map(documentMapper::map).toList();
    }
}
