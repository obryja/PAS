package org.example.rest.controllers;

import jakarta.validation.Valid;
import org.example.rest.dto.UserCuDTO;
import org.example.rest.dto.UserGetDTO;
import org.example.rest.mappers.UserMapper;
import org.example.rest.models.Admin;
import org.example.rest.models.Client;
import org.example.rest.models.Manager;
import org.example.rest.models.User;
import org.example.rest.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper = new UserMapper();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserGetDTO> userGetDTOs =  users.stream()
                .map(userMapper::userToUserGetDTO)
                .toList();
        return ResponseEntity.ok(userGetDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.userToUserGetDTO(user));
    }

    @PostMapping("/client")
    public ResponseEntity<User> createClient(@RequestBody @Valid UserCuDTO user) {
        User newUser = new Client(user.getUsername(), user.getPassword(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(newUser));
    }

    @PostMapping("/manager")
    public ResponseEntity<User> createManager(@RequestBody @Valid UserCuDTO user) {
        User newUser = new Manager(user.getUsername(), user.getPassword(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(newUser));
    }

    @PostMapping("/admin")
    public ResponseEntity<User> createAdmin(@RequestBody @Valid UserCuDTO user) {
        User newUser = new Admin(user.getUsername(), user.getPassword(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(newUser));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserGetDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userMapper.userToUserGetDTO(user));
    }

    @GetMapping("/username")
    public ResponseEntity<List<UserGetDTO>> searchUserByUsername(@RequestParam String username) {
        List<User> users = userService.getUsersByUsername(username);
        List<UserGetDTO> userGetDTOs =  users.stream()
                .map(userMapper::userToUserGetDTO)
                .toList();
        return ResponseEntity.ok(userGetDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody @Valid UserCuDTO user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user.getUsername(), user.getPassword()));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<UserGetDTO> activateUser(@PathVariable String id) {
        User user = userService.activateUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToUserGetDTO(user));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<UserGetDTO> deactivateUser(@PathVariable String id) {
        User user = userService.deactivateUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToUserGetDTO(user));
    }

    @GetMapping("/active/client")
    public ResponseEntity<List<UserGetDTO>> getActiveUsers() {
        List<User> users = userService.getActiveClients();
        List<UserGetDTO> userGetDTOs =  users.stream()
                .map(userMapper::userToUserGetDTO)
                .toList();
        return ResponseEntity.ok(userGetDTOs);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<User> getUserDetailsById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
