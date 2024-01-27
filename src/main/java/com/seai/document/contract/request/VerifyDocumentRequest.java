package com.seai.document.contract.request;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class VerifyDocumentRequest {

    private String name;
    private String number;
    private Date issueDate;
    private Date expiryDate;
    private Instant notifyAt;
    private boolean isVerified;
}
