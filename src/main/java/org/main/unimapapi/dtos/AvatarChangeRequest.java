package org.main.unimapapi.dtos;

import lombok.Data;

@Data
public class AvatarChangeRequest {
    private String email;
    private byte[] avatarBinary;
}