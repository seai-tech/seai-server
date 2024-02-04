package com.seai.marine.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserAuthentication {

    private UUID id;

    private String email;

    private String password;
}
