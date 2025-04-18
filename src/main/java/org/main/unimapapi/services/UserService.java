package org.main.unimapapi.services;

import lombok.AllArgsConstructor;
import org.main.unimapapi.dtos.User_dto;
import org.main.unimapapi.entities.User;
import org.main.unimapapi.repository_queries.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(User_dto dto) {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setUsername(dto.getUsername());
        user.setAdmin(dto.isAdmin());
        user.setPremium(dto.isPremium());
        user.setAvatar(dto.getAvatarBinary().getBytes());
        user.setAvatarFileName(dto.getAvatarFileName());
        userRepository.save(user);
        return user;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User update(User user) {
        userRepository.update(user);
        return user;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void delete_all_user_info(Long id) {
        userRepository.deleteAllUserInfo(id);
    }
    public void delete_all_user_comments(Long id) {
        userRepository.deleteAllUserComments(id);
    }

}