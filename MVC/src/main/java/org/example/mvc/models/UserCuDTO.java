package org.example.mvc.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCuDTO {
    @NotNull
    @Size(min = 3, max = 50, message = "{username.size}")
    private String username;

    @NotNull
    @Size(min = 3, max = 50, message = "{password.size}")
    private String password;

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
}