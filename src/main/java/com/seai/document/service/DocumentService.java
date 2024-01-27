package com.seai.document.service;

import com.seai.document.contract.request.UpdateDocumentRequest;
import com.seai.document.mapper.DocumentMapper;
import com.seai.document.model.MarineDocument;
import com.seai.document.repository.DocumentRepository;
import com.seai.notification.model.DocumentNotification;
import com.seai.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final NotificationRepository notificationRepository;
    private final DocumentMapper documentMapper;

    public void verifyDocument(UpdateDocumentRequest updateDocumentRequest, UUID userId, UUID documentId) {
        MarineDocument marineDocument = documentMapper.map(updateDocumentRequest);
        documentRepository.verify(marineDocument, userId, documentId);
//        List<DocumentNotification> notifications = createNotifications(verifyDocumentRequest, documentId);
//
//        for (DocumentNotification notification : notifications) {
//            notificationRepository.saveNotification(notification);
//        }
    }

    private List<DocumentNotification> createNotifications(UpdateDocumentRequest updateDocumentRequest, UUID documentId) {
//        List<DocumentNotification> documentNotifications = new ArrayList<>();
//        for (Integer daysBefore : verifyDocumentRequest.getNotifyBefore()) {
//            Date toNotifyDate = DateUtils.addDays(verifyDocumentRequest.getExpiryDate(), -daysBefore);
//            documentNotifications.add(new DocumentNotification(documentId,verifyDocumentRequest.getNotifyAt(), toNotifyDate));
//        }
        return null;
    }
}
