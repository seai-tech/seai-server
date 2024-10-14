package com.seai.marine.next_of_kin.contract.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NextOfKinCreateRequest {

    private String name;

    private String surname;

    private String phone;

    private String address;

    private String email;
}
