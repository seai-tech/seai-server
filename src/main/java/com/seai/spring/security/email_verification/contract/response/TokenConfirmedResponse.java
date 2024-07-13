package com.seai.spring.security.email_verification.contract.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenConfirmedResponse {
    private String message;
}
