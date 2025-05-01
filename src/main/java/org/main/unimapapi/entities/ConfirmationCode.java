package org.main.unimapapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationCode {
    private Long userId;
    private String code;
    private LocalDateTime expirationTime;
}