package org.example.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.rest.enums.Role;

public class UserCreateDTO {
    @NotNull
    @Size(min = 3, message = "Nazwa użytkownika musi mieć przynajmniej 3 znaki")
    private String username;

    @NotNull
    @Size(min = 3, message = "Hasło musi mieć przynajmniej 3 znaki")
    private String password;

    @NotNull
    private boolean active;

    @NotNull
    private Role role;

    public UserCreateDTO(String id, String username, String password, boolean active, Role role) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
