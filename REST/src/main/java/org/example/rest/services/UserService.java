package org.example.rest.services;

import org.example.rest.dto.UserGetDTO;
import org.example.rest.dto.UserUpdateDTO;
import org.example.rest.models.User;
import org.example.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public UserGetDTO getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole());
    }

    public List<UserGetDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole()))
                .collect(Collectors.toList());
    }

    public UserGetDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole());
    }

    public List<UserGetDTO> getUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username).stream()
                .map(user -> new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole()))
                .collect(Collectors.toList());
    }

    public User updateUser(String id, UserUpdateDTO changedUser) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(changedUser.getUsername());
        existingUser.setPassword(changedUser.getPassword());

        return userRepository.save(existingUser);
    }

    public UserGetDTO activateUser(String id) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setActive(true);
        User user = userRepository.save(existingUser);

        return new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole());
    }

    public UserGetDTO deactivateUser(String id) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setActive(false);
        User user = userRepository.save(existingUser);

        return new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole());
    }
}
