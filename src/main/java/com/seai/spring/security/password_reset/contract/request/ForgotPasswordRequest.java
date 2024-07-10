package com.seai.spring.security.password_reset.contract.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForgotPasswordRequest {
    private String newPassword;
    private String confirmPassword;
}
