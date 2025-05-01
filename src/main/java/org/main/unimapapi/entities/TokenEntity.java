package org.main.unimapapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenEntity {
    private Long id;
    private String refreshToken;
    private boolean revoked;
    private LocalDateTime expiryDate;
    private Long userId;
    private LocalDateTime createdAt;

    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}