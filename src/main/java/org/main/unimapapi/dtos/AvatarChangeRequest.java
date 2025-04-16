package org.main.unimapapi.dtos;

public class AvatarChangeRequest {
    private String email;
    private byte[] avatarBinary;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getAvatarBinary() {
        return avatarBinary;
    }

    public void setAvatarBinary(byte[] avatarBinary) {
        this.avatarBinary = avatarBinary;
    }
}