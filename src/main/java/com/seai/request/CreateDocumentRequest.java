package com.seai.request;

import lombok.Data;

import java.util.Date;

@Data
public class CreateDocumentRequest {

    private String name;
    private String number;
    private Date issueDate;
    private Date expiryDate;
}
