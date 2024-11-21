package org.example.rest.controllers;

import jakarta.validation.Valid;
import org.example.rest.dto.UserCreateDTO;
import org.example.rest.dto.UserGetDTO;
import org.example.rest.dto.UserUpdateDTO;
import org.example.rest.models.User;
import org.example.rest.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserCreateDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserGetDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/username")
    public ResponseEntity<List<UserGetDTO>> searchUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUsersByUsername(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateDTO user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<UserGetDTO> activateUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.activateUser(id));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<UserGetDTO> deactivateUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deactivateUser(id));
    }
}
