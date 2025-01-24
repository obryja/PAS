package org.example.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserPasswordDTO {
    @NotNull
    @Size(min = 3, max = 50, message = "Hasło musi mieć od 3 do 50 znaków")
    private String password;

    @JsonCreator
    public UserPasswordDTO(@JsonProperty("password") String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
