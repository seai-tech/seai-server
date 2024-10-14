package com.seai.marine.next_of_kin.model;

import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Data
@Setter
public class NextOfKin {

    private UUID id;

    private String name;

    private String surname;

    private String phone;

    private String address;

    private String email;

    private UUID userId;
}
