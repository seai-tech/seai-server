package com.seai.training_center.training_center.contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingCenterAuthenticationResponse {

    private String trainingCenterId;

    private String accessToken;
}
