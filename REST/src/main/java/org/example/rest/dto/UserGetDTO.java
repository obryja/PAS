package org.example.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.rest.enums.Role;

public class UserGetDTO {
    @NotNull
    private String id;

    @NotNull
    @Size(min = 3, max = 50, message = "Nazwa użytkownika musi mieć od 3 do 50 znaków")
    private String username;

    @NotNull
    private boolean active;

    @NotNull
    private Role role;

    public UserGetDTO(String id, String username, boolean active, Role role) {
        this.id = id;
        this.username = username;
        this.active = active;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
