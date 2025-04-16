package org.main.unimapapi.services;

import org.main.unimapapi.dtos.User_dto;
import org.main.unimapapi.entities.User;
import org.main.unimapapi.repository_queries.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class RegistrationService {
    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User_dto dto) {
        try {
            User user = User.builder()
                    .username(dto.getUsername())
                    .email(dto.getEmail())
                    .login(dto.getLogin())
                    .password(dto.getPassword())
                    .isAdmin(dto.isAdmin())
                    .isPremium(dto.isPremium())
                    .avatar(dto.getAvatarBinary() != null ? Base64.getDecoder().decode(dto.getAvatarBinary()) : null)
                    .avatarFileName(dto.getAvatarFileName())
                    .build();

            userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error during user registration: " + e.getMessage(), e);
        }
    }
}