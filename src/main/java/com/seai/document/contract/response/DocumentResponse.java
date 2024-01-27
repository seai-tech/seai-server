package com.seai.document.contract.response;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class DocumentResponse {

    private UUID id;
    private String name;
    private String number;
    private Date issueDate;
    private Date expiryDate;
    private Instant createdDate;
    private boolean isVerified;
}
