package com.seai.marine.document.contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GetDocumentsForReminderResponse {
    private UUID id;
    private String name;
    private String number;
    private Date issueDate;
    private Date expiryDate;
    private boolean isVerified;
    private Instant createdDate;
    private UUID userId;
    private String path;
    private String userEmail;
}
