package org.example.rest.controllers;

import org.example.rest.dto.UserGetDTO;
import org.example.rest.dto.UserUpdateDTO;
import org.example.rest.models.User;
import org.example.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserGetDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public UserGetDTO getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public UserGetDTO getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/search")
    public List<UserGetDTO> searchUserByUsername(@RequestParam String username) {
        return userService.getUsersByUsername(username);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody UserUpdateDTO user) {
        return userService.updateUser(id, user);
    }

    @PutMapping("/{id}/activate")
    public UserGetDTO activateUser(@PathVariable String id) {
        return userService.activateUser(id);
    }

    @PutMapping("/{id}/deactivate")
    public UserGetDTO deactivateUser(@PathVariable String id) {
        return userService.deactivateUser(id);
    }
}
