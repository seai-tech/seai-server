package com.seai.document.contract.request;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateDocumentRequest {

    private String name;
    private String number;
    private Date issueDate;
    private Date expiryDate;
    private boolean isVerified;
}
