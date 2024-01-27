package com.seai.document.controller;

import com.seai.document.contract.request.CreateDocumentRequest;
import com.seai.document.contract.request.UpdateDocumentRequest;
import com.seai.document.contract.response.GetDocumentResponse;
import com.seai.document.mapper.DocumentMapper;
import com.seai.document.model.MarineDocument;
import com.seai.document.repository.DocumentRepository;
import com.seai.document.service.DocumentScanner;
import com.seai.document.service.DocumentUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    private final DocumentUploadService documentUploadService;
    private final DocumentMapper documentMapper;

    //OCR
    @PostMapping(value = "/users/{userId}/ocr", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public GetDocumentResponse upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID userId) {
        MarineDocument marineDocument = documentScanner.readDocument(multipartFile);
        documentRepository.save(marineDocument, userId);
        documentUploadService.upload(multipartFile, marineDocument.getPath());
        return documentMapper.map(marineDocument);
    }

    //CREATE
    @PostMapping("/users/{userId}/documents")
    public void create(@PathVariable UUID userId, @RequestBody CreateDocumentRequest createDocumentRequest) {
        MarineDocument verifiedDocument = MarineDocument.createVerifiedDocument(createDocumentRequest.getName(),
                createDocumentRequest.getNumber(),
                createDocumentRequest.getIssueDate(),
                createDocumentRequest.getExpiryDate());
        documentRepository.save(verifiedDocument, userId);
    }

    //READ
    @GetMapping("/users/{userId}/documents/{documentId}")
    public GetDocumentResponse find(@PathVariable UUID userId, @PathVariable UUID documentId) {
        return documentMapper.map(documentRepository.find(userId, documentId));
    }

    //UPDATE
    @PutMapping("/users/{userId}/documents/{documentId}")
    public void update(@RequestBody UpdateDocumentRequest updateDocumentRequest, @PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument marineDocument = documentMapper.map(updateDocumentRequest);
        documentRepository.update(marineDocument, userId, documentId);
    }

    //DELETE
    @DeleteMapping("/users/{userId}/documents/{documentId}")
    public void delete(@PathVariable UUID userId, @PathVariable UUID documentId) {
        MarineDocument document = documentRepository.find(userId, documentId);
        documentUploadService.delete(document.getPath());
    }

    //FIND ALL
    @GetMapping("/users/{userId}/documents")
    public List<GetDocumentResponse> findAll(@PathVariable UUID userId) {
        return documentRepository.findAll(userId).stream().map(documentMapper::map).toList();
    }
}
