package com.seai.domain.document.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MarineDocument {
    private UUID id;
    private String name;
    private String number;
    private Date issueDate;
    private Date expiryDate;
    private Instant createdDate;
    private String path;
    private boolean isVerified;

    public MarineDocument(UUID id, String name, String number, Date issueDate, Date expiryDate, boolean isVerified, Instant createdDate, String path) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.isVerified = isVerified;
        this.createdDate = createdDate;
        this.path = path;
    }

    public static MarineDocument createNonVerifiedDocument(String name, String number, Date issueDate, Date expiryDate) {
        return new MarineDocument(UUID.randomUUID(), name, number, issueDate, expiryDate, false, Instant.now(), null);
    }

    public static MarineDocument createVerifiedDocument(String name, String number, Date issueDate, Date expiryDate) {
        return new MarineDocument(UUID.randomUUID(), name, number, issueDate, expiryDate, true, Instant.now(), null);
    }
}
