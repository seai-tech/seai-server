package com.seai.spring.security.token.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken {
    private UUID id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;
    private UUID userId;
}
