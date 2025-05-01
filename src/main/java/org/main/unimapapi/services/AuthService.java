package org.main.unimapapi.services;

import lombok.AllArgsConstructor;
import org.main.unimapapi.entities.User;
import org.main.unimapapi.repository_queries.UserRepository;
import org.main.unimapapi.utils.Hashing;
import org.main.unimapapi.utils.ServerLogger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User authenticate(String login, String password) {
        try {
            Optional<User> userOptional = userRepository.findByLogin(login);
            if (userOptional.isEmpty()) {
                userOptional = userRepository.findByEmail(login);
            }

            if (userOptional.isPresent() && Hashing.checkPassword(password, userOptional.get().getPassword())) {
                return userOptional.get();
            }

            ServerLogger.logServer(ServerLogger.Level.WARNING, "User not found!");
            return null;
        } catch (Exception e) {
         //   System.err.println("Error during authentication: " + e.getMessage());
            ServerLogger.logServer(ServerLogger.Level.ERROR, "Error during authentication: " + e.getMessage());
            return null;
        }
    }
}