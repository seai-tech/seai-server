package com.seai.marine.user.contract.response;

import com.seai.marine.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserResponse {
    private String message;
    private User user;
}
