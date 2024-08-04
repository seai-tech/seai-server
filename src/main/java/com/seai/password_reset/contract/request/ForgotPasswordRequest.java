package com.seai.password_reset.contract.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class ForgotPasswordRequest {
    private String newPassword;
    private String confirmPassword;

    public boolean arePasswordsMatching() {
        return Objects.equals(newPassword, confirmPassword);
    }
}
