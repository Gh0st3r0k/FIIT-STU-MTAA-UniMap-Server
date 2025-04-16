package org.main.unimapapi.services;

import org.main.unimapapi.entities.User;
import org.main.unimapapi.repository_queries.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ChangeAvatarService {

    @Autowired
    private UserRepository userRepository;

    public boolean updateAvatarData(String login, byte[] avatarBinary, String fileName) {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAvatar(avatarBinary);
            user.setAvatarFileName(fileName);
            userRepository.update(user);
            System.out.println("Avatar updated successfully for user: " + login);
            return true;
        }
        System.out.println("User dontt found: " + login);
        return false;
    }
}