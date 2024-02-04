package com.seai.marine.document.contract.response;

import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
public class GetDocumentResponse {

    private UUID id;
    private String name;
    private String number;
    private Date issueDate;
    private Date expiryDate;
    private Instant createdDate;
    private boolean isVerified;
}
