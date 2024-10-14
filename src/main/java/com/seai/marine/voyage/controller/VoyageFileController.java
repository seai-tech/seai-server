package com.seai.marine.voyage.controller;

import com.seai.marine.voyage.contract.response.GetVoyageResponse;
import com.seai.marine.voyage.repository.VoyageRepository;
import com.seai.marine.voyage.service.VoyageFileService;
import com.seai.marine.voyage.service.VoyageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/{userId}/voyages/{voyageId}/files")
public class VoyageFileController {

    private final VoyageService voyageService;

    private final VoyageFileService voyageFileService;

    private static final String AUTHORIZATION = "#userId.equals(authentication.principal.id)";

    @PostMapping
    @PreAuthorize(AUTHORIZATION)
    public void upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable UUID userId, @PathVariable UUID voyageId) {
        GetVoyageResponse voyage = voyageService.getById(userId, voyageId);
        String path = String.format("%s/%s/%s", userId, "voyage", voyage.getId());
        voyageFileService.upload(multipartFile, path);
    }

    @DeleteMapping
    @PreAuthorize(AUTHORIZATION)
    public void delete(@PathVariable UUID userId, @PathVariable UUID voyageId) {
        GetVoyageResponse voyage = voyageService.getById(userId, voyageId);
        String path = String.format("%s/%s/%s", userId, "voyage", voyage.getId());
        voyageFileService.delete(path);
    }

    @GetMapping
    @PreAuthorize(AUTHORIZATION)
    public ResponseEntity<byte[]> download(@PathVariable UUID userId, @PathVariable UUID voyageId) {
        GetVoyageResponse voyage = voyageService.getById(userId, voyageId);
        String path = String.format("%s/%s/%s", userId, "voyage", voyage.getId());
        byte[] bytes = voyageFileService.download(path);
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
