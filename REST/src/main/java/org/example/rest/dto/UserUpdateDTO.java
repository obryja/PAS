package org.example.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    @NotNull
    @Size(min = 3, message = "Nazwa użytkownika musi mieć przynajmniej 3 znaki")
    private String username;

    @NotNull
    @Size(min = 3, message = "Hasło musi mieć przynajmniej 3 znaki")
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

