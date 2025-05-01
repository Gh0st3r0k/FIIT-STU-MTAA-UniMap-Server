package org.main.unimapapi.utils;

import lombok.RequiredArgsConstructor;
import org.main.unimapapi.configs.AppConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Service for sending emails asynchronously
 */
@Service
@RequiredArgsConstructor
public class EmailSender {

    @Async
    public void sendVerificationCode(String recipient, String code) {
        String sender = AppConfig.getSender();
        String host = AppConfig.getHost();
        String port = AppConfig.getPort();
        String password = AppConfig.getPassword();

        Properties properties = createMailProperties(host, port);
        Session session = createMailSession(sender, password, properties);

        try {
            MimeMessage message = createMessage(session, sender, recipient, code);
            Transport.send(message);
            ServerLogger.logServer(ServerLogger.Level.INFO, String.format("Verification code sent to %s", recipient));
        } catch (MessagingException mex) {
            ServerLogger.logServer(ServerLogger.Level.ERROR, String.format("Failed to send verification code to %s: %s", recipient, mex.getMessage()));
        }
    }

    private Properties createMailProperties(String host, String port) {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        return properties;
    }

    private Session createMailSession(String sender, String password, Properties properties) {
        return Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });
    }

    private MimeMessage createMessage(Session session, String sender, String recipient, String code)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(sender));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject("UniMap Confirmation Code");

        String emailBody = String.format(
                """
                        Hi, your confirmation code is: %s
                        
                        This code will expire in 5 minutes!
                        
                        Best regards, UniMap Team""",
                code);

        message.setText(emailBody);
        return message;
    }
}