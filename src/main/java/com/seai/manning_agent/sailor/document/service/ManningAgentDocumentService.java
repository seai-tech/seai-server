package com.seai.manning_agent.sailor.document.service;

import com.seai.exception.ResourceNotFoundException;
import com.seai.manning_agent.sailor.document.contract.request.CreateDocumentRequest;
import com.seai.manning_agent.sailor.document.contract.request.UpdateDocumentRequest;
import com.seai.manning_agent.sailor.document.contract.response.CreateDocumentResponse;
import com.seai.manning_agent.sailor.document.contract.response.GetDocumentResponse;
import com.seai.manning_agent.sailor.document.mapper.ManningAgentDocumentMapper;
import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.repository.ManningAgentDocumentRepository;
import com.seai.manning_agent.sailor.service.ManningAgentSailorService;
import com.seai.marine.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManningAgentDocumentService {

    @Value("${scanner.aws.bucket.name}")
    private String bucketName;
    private final ManningAgentDocumentScanner documentScanner;
    private final ManningAgentDocumentRepository documentRepository;
    private final ManningAgentDocumentFileService documentFileService;
    private final ManningAgentDocumentMapper documentMapper;
    private final ManningAgentSailorService manningAgentSailorService;


    public GetDocumentResponse upload(MultipartFile multipartFile, UUID manningAgentId, UUID sailorId) {
        Optional<User> sailor = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        MarineDocument marineDocument = documentScanner.readDocument(multipartFile);
        documentRepository.save(marineDocument, sailor.get().getId());
        documentFileService.upload(multipartFile, marineDocument.getPath());
        return documentMapper.map(marineDocument);
    }

    public CreateDocumentResponse create(UUID manningAgentId, UUID sailorId, CreateDocumentRequest createDocumentRequest) {
        Optional<User> sailor = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        MarineDocument verifiedDocument = MarineDocument.createVerifiedDocument(createDocumentRequest.getName(),
                createDocumentRequest.getNumber(),
                createDocumentRequest.getIssueDate(),
                createDocumentRequest.getExpiryDate());
        return documentMapper.mapCreate(documentRepository.save(verifiedDocument, sailor.get().getId()));
    }

    public GetDocumentResponse find(UUID manningAgentId, UUID sailorId, UUID documentId) {
        Optional<User> sailor = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        return documentMapper.map(documentRepository.find(sailor.get().getId(), documentId));
    }

    public void update(UpdateDocumentRequest updateDocumentRequest, UUID manningAgentId, UUID sailorId, UUID documentId) {
        Optional<User> sailor = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        MarineDocument marineDocument = documentMapper.map(updateDocumentRequest);
        documentRepository.update(marineDocument, sailor.get().getId(), documentId);
    }

    @Transactional
    public void delete(UUID manningAgentId, UUID sailorId, UUID documentId) {
        Optional<User> sailor = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        if (sailor.isEmpty()) {
            return;
        }
        try {
            MarineDocument document = documentRepository.find(sailor.get().getId(), documentId);
            documentFileService.delete(document.getPath());
            documentRepository.delete(documentId, sailor.get().getId());
        } catch (ResourceNotFoundException ignored) {
        }
    }

    public List<GetDocumentResponse> findAll(UUID manningAgentId, UUID sailorId) {
        Optional<User> sailor = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        return documentRepository.findAll(sailor.get().getId()).stream().map(documentMapper::map).toList();
    }
}
