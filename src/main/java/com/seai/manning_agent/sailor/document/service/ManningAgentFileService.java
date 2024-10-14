package com.seai.manning_agent.sailor.document.service;

import com.seai.manning_agent.sailor.document.model.MarineDocument;
import com.seai.manning_agent.sailor.document.repository.ManningAgentDocumentRepository;
import com.seai.manning_agent.sailor.service.ManningAgentSailorService;
import com.seai.marine.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ManningAgentFileService {

    private final ManningAgentDocumentRepository documentRepository;
    private final ManningAgentDocumentFileService documentFileService;
    private final ManningAgentSailorService manningAgentSailorService;


    public void upload(MultipartFile multipartFile, UUID manningAgentId, UUID sailorId, UUID documentId) {
        Optional<User> sailor = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        MarineDocument document = documentRepository.find(sailor.get().getId(), documentId);
        documentFileService.upload(multipartFile, document.getPath());
    }

    public void delete(UUID manningAgentId, UUID sailorId, UUID documentId) {
        Optional<User> sailor = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        MarineDocument document = documentRepository.find(sailor.get().getId(), documentId);
        documentFileService.delete(document.getPath());
    }

    public ResponseEntity<byte[]> download(UUID manningAgentId, UUID sailorId, UUID documentId) {
        Optional<User> sailor = manningAgentSailorService.getSailorById(manningAgentId, sailorId);
        MarineDocument document = documentRepository.find(sailor.get().getId(), documentId);
        byte[] bytes = documentFileService.download(document.getPath());
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
