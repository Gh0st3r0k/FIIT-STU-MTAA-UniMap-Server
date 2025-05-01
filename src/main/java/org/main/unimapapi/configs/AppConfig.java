package org.main.unimapapi.configs;


import lombok.Data;
import lombok.Getter;
import org.main.unimapapi.utils.ServerLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * UniMap application configuration class
 *
 * Responsible for:
 * - Connection to the mail server (SMTP)
 * - Loading sensitive data from config.properties
 * - OAuth2 settings (Google and Facebook)
 * - Encryption (AES)
 */
@Configuration
@Data
public class AppConfig {
    @Getter
    private static final String sender = "unimapofficial@gmail.com";

    // Google's SMTP server
    @Getter
    private static final String host = "smtp.gmail.com";
    // SMTP server port
    @Getter
    private static final String port = "587";

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = AppConfig.class.getResourceAsStream("/org.main.unimapapi/config.properties")
        ) {
            if (input != null) {
                properties.load(input);
            } else {
                ServerLogger.logServer(ServerLogger.Level.ERROR, "config.properties file is missing.");
                throw new RuntimeException("config.properties file is null");
            }
        } catch (IOException ex) {
            ServerLogger.logServer(ServerLogger.Level.ERROR, "Failed to load application.properties: " + ex.getMessage());
            throw new RuntimeException("Failed to load application.properties: " + ex.getMessage(), ex);
        }
    }

    public static String getAesAlgorithm() {
        return properties.getProperty("AES_ALGORITHM");
    }

    public static String getSecretKey() {
        return properties.getProperty("SECRET_KEY");
    }

    public static String getIv() {
        return properties.getProperty("IV");
    }

    public static String getPassword() {
        return properties.getProperty("PASSWORD_email");
    }

    public static String getAccessKey() {
        return properties.getProperty("ACCESS_SECRET_KEY");
    }

    public static String getRefreshKey() {
        return properties.getProperty("REFRESH_SECRET_KEY");
    }

    @Getter
    private static final String Oauth2Google_id = "test";
    @Getter
    private static final String Oauth2Google_secret = "test";

    @Getter
    private static final String Oauth2Facebook_id = "test";
    @Getter
    private static final String Oauth2Facebook_secret = "test";

    @Getter
    private static final long EXPIRATION_TIME_ACCESS = 60000;

    @Getter
    private static final long EXPIRATION_TIME_REFRESH = 86400000;

    public static String getLogLevel() {
        return properties.getProperty("LOG_LEVEL", "INFO");
    }

    @Getter
    private static final String SERVER_LOG_FILE = "src/main/resources/org.main.unimapapi/logs/server_logs.xml";
    @Getter
    private static final String CLIENT_LOG_FILE = "src/main/resources/org.main.unimapapi/logs/client_logs.xml";

}