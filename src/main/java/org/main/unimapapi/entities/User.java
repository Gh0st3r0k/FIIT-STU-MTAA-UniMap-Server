package org.main.unimapapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String login;
    private String email;
    private String password;
    private String username;
    private boolean isAdmin;
    private boolean isPremium;
    private byte[] avatar;
    private String avatarFileName;
}