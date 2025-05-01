package org.main.unimapapi.services;

import org.main.unimapapi.entities.ConfirmationCode;
import org.main.unimapapi.repository_queries.ConfirmationCodeRepository;
import org.main.unimapapi.utils.ServerLogger;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ConfirmationCodeService {
    private final ConfirmationCodeRepository confirmationCodeRepository;

    public void save(ConfirmationCode confirmationCode) {
        confirmationCodeRepository.save(confirmationCode);
    }

    public static String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }


    public boolean validateConfirmationCode(Long userId, String code) {
        try {
            ServerLogger.logServer(ServerLogger.Level.INFO, "VALIDATE CONFIRMATION CODE "+userId+" "+code);
            if (confirmationCodeRepository.find(userId, code)) {
                confirmationCodeRepository.deleteByUserId(userId, code);
                return true;
            }
            return false;
        } catch (Exception e) {
            ServerLogger.logServer(ServerLogger.Level.ERROR, "Error during confirmation code validation: " + e.getMessage());
            return false;
        }
    }


}