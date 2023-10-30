package com.seai.domain.model;

import lombok.Data;

@Data
public class SeaiUser {

    private final String email;

    private final String password;

    private final String firstName;

    private final String lastName;

}
