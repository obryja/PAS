package org.example.rest.dto;

import org.example.rest.enums.Role;

public class UserGetDTO {
    private String id;
    private String username;
    private boolean active;
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
