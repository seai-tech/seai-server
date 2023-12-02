package com.seai.request;

import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
public class VerifyDocumentRequest {

    private String name;
    private String number;
    private Date issueDate;
    private Date expiryDate;
    private Instant notifyAt;
    private List<Integer> notifyBefore;
}
