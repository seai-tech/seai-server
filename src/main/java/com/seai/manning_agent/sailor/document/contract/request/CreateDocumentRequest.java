package com.seai.manning_agent.sailor.document.contract.request;

import lombok.Data;

import java.util.Date;

@Data
public class CreateDocumentRequest {

    private String name;
    private String number;
    private Date issueDate;
    private Date expiryDate;
    private boolean isVerified;
}
