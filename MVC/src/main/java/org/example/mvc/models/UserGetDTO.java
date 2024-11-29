package org.example.mvc.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.mvc.enums.Role;

public class UserGetDTO {
    @NotNull
    private String id;

    @NotNull
    @Size(min = 3, max = 50, message = "{username.size}")
    private String username;

    @NotNull
    private boolean active;

    @NotNull
    private Role role;

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
