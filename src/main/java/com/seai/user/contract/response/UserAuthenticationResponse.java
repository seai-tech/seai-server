package com.seai.user.contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthenticationResponse {

    private String userId;

    private String accessToken;
}
