package org.main.unimapapi.configs;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SecurityConfig {
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "0123456789abcdef0123456789abcdef"; // 32-byte key
    private static final String IV = "0123456789abcdef"; // 16-byte IV

    public static String getAesAlgorithm() {
        return AES_ALGORITHM;
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public static String getIv() {
        return IV;
    }
}