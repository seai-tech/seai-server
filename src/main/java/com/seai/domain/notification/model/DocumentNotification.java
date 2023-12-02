package com.seai.domain.notification.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DocumentNotification {
    private UUID id;
    private UUID documentId;
    private Instant notifyAt;
    private Date notifyDay;

    public DocumentNotification(UUID documentId, Instant notifyAt, Date notifyDay) {
        this.id = UUID.randomUUID();
        this.documentId = documentId;
        this.notifyAt = notifyAt;
        this.notifyDay = notifyDay;
    }
}
