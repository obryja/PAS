package org.example.rest.controllers;

import jakarta.validation.Valid;
import org.example.rest.dto.UserCuDTO;
import org.example.rest.dto.UserGetDTO;
import org.example.rest.dto.UserPasswordDTO;
import org.example.rest.exceptions.UnauthorizedException;
import org.example.rest.mappers.UserMapper;
import org.example.rest.models.Admin;
import org.example.rest.models.Client;
import org.example.rest.models.Manager;
import org.example.rest.models.User;
import org.example.rest.security.JwtUtils;
import org.example.rest.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper = new UserMapper();
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCuDTO authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            String token = jwtUtils.generateToken(authentication);
            return ResponseEntity.ok(token);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nieprawidłowe dane logowania");
        }
    }

    @PostMapping("/register/client")
    public ResponseEntity<UserGetDTO> createClient(@RequestBody @Valid UserCuDTO user) {
        User newUser = new Client(user.getUsername(), user.getPassword(), true);
        User user1 = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToUserGetDTO(user1));
    }

    @PostMapping("/register/manager")
    public ResponseEntity<UserGetDTO> createManager(@RequestBody @Valid UserCuDTO user) {
        User newUser = new Manager(user.getUsername(), user.getPassword(), true);
        User user1 = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToUserGetDTO(user1));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<UserGetDTO> createAdmin(@RequestBody @Valid UserCuDTO user) {
        User newUser = new Admin(user.getUsername(), user.getPassword(), true);
        User user1 = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToUserGetDTO(user1));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserGetDTO> userGetDTOs =  users.stream()
                .map(userMapper::userToUserGetDTO)
                .toList();
        return ResponseEntity.ok(userGetDTOs);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.userToUserGetDTO(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/username/{username}")
    public ResponseEntity<UserGetDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userMapper.userToUserGetDTO(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/username")
    public ResponseEntity<List<UserGetDTO>> searchUserByUsername(@RequestParam String username) {
        List<User> users = userService.getUsersByUsername(username);
        List<UserGetDTO> userGetDTOs =  users.stream()
                .map(userMapper::userToUserGetDTO)
                .toList();
        return ResponseEntity.ok(userGetDTOs);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserGetDTO> updateUser(@PathVariable String id, @RequestBody @Valid UserPasswordDTO user) {
        User user1 = userService.updateUser(id, user.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToUserGetDTO(user1));
    }

    @PostMapping("/password")
    public ResponseEntity<UserGetDTO> changePassword(@RequestBody @Valid UserPasswordDTO user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user1 = userService.changePassword(authentication.getName(), user.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToUserGetDTO(user1));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/{id}/activate")
    public ResponseEntity<UserGetDTO> activateUser(@PathVariable String id) {
        User user = userService.activateUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToUserGetDTO(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<UserGetDTO> deactivateUser(@PathVariable String id) {
        User user = userService.deactivateUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToUserGetDTO(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/active/client")
    public ResponseEntity<List<UserGetDTO>> getActiveUsers() {
        List<User> users = userService.getActiveClients();
        List<UserGetDTO> userGetDTOs =  users.stream()
                .map(userMapper::userToUserGetDTO)
                .toList();
        return ResponseEntity.ok(userGetDTOs);
    }

    @GetMapping("/me")
    public ResponseEntity<UserGetDTO> getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(userMapper.userToUserGetDTO(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}/details")
    public ResponseEntity<UserGetDTO> getUserDetailsById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.userToUserGetDTO(user));
    }
}
