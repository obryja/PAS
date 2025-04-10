package org.example.rest.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.*;
import org.example.rest.enums.Role;

@BsonDiscriminator(key = "_clazz", value = "user")
public abstract class User {
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;

    @BsonProperty("username")
    @NotNull
    @Size(min = 3, max = 50, message = "Nazwa użytkownika musi mieć od 3 do 50 znaków")
    private String username;

    @BsonProperty("password")
    @NotNull
    @Size(min = 3, max = 50, message = "Hasło musi mieć od 3 do 50 znaków")
    private String password;

    @BsonProperty("active")
    @NotNull
    private boolean active;

    @BsonProperty("role")
    @NotNull
    private Role role;

    @BsonCreator
    public User(@BsonProperty("username") String username, @BsonProperty("password") String password, @BsonProperty("active") boolean active, @BsonProperty Role role) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.role = role;
    }

    public User() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername(){
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

    public boolean isActive(){
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

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", active=" + active + ", role=" + getRole() + "]";
    }
}
