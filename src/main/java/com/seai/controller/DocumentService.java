package com.seai.controller;

import com.seai.domain.document.model.MarineDocument;
import com.seai.domain.document.repository.DocumentRepository;
import com.seai.domain.notification.model.DocumentNotification;
import com.seai.domain.notification.repository.NotificationRepository;
import com.seai.mapper.DocumentMapper;
import com.seai.request.VerifyDocumentRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final NotificationRepository notificationRepository;
    private final DocumentMapper documentMapper;

    public void verifyDocument(VerifyDocumentRequest verifyDocumentRequest, UUID userId, UUID documentId) {
        MarineDocument marineDocument = documentMapper.map(verifyDocumentRequest);
        documentRepository.verify(marineDocument, userId, documentId);
        List<DocumentNotification> notifications = createNotifications(verifyDocumentRequest, documentId);

        for (DocumentNotification notification : notifications) {
            notificationRepository.saveNotification(notification);
        }
    }

    private List<DocumentNotification> createNotifications(VerifyDocumentRequest verifyDocumentRequest, UUID documentId) {
        List<DocumentNotification> documentNotifications = new ArrayList<>();
        for (Integer daysBefore : verifyDocumentRequest.getNotifyBefore()) {
            Date toNotifyDate = DateUtils.addDays(verifyDocumentRequest.getExpiryDate(), -daysBefore);
            documentNotifications.add(new DocumentNotification(documentId,verifyDocumentRequest.getNotifyAt(), toNotifyDate));
        }
        return documentNotifications;
    }
}
