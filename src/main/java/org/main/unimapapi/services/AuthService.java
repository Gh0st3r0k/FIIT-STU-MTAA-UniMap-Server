package org.main.unimapapi.services;

import lombok.AllArgsConstructor;
import org.main.unimapapi.dtos.User_dto;
import org.main.unimapapi.entities.User;
import org.main.unimapapi.repositories.UserRepository;
import org.main.unimapapi.utils.Decryptor;
import org.main.unimapapi.utils.Encryptor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User register(User_dto dto) throws Exception {
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .password(Encryptor.encrypt(dto.getPassword()))
                .isAdmin(dto.isAdmin())
                .subscribe(dto.isSubscribe())
                .verification(dto.isVerification())
                .avatar(dto.getAvatar())
                .build();
        return userRepository.save(user);
    }

    public User authenticate(String login, String password) throws Exception {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            System.out.println("User found: " + user.get().getLogin());
            if (user.get().getPassword().equals(password)) {
                System.out.println("User authenticated: " + user.get().getLogin());
                return user.get();
            }
            return null;
        }
        return null;
    }
}