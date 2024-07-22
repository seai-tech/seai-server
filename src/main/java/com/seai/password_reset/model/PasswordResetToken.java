package com.seai.password_reset.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PasswordResetToken {
    private UUID id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private UUID userId;

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}

