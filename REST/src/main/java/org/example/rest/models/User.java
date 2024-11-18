package org.example.rest.models;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.*;
import org.example.rest.enums.Role;

@BsonDiscriminator(key = "_clazz", value = "user")
public abstract class User {
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;

    @BsonProperty("username")
    private String username;

    @BsonProperty("password")
    private String password;

    @BsonProperty("active")
    private boolean active;

    @BsonCreator
    public User(@BsonProperty("username") String username, @BsonProperty("password") String password, @BsonProperty("active") boolean active) {
        this.username = username;
        this.password = password;
        this.active = active;
    }

    public User() {}

    public static User createUser(String username, String password, boolean active, Role role) {
        switch (role) {
            case ROLE_ADMIN:
                return new Admin(username, password, active);
            case ROLE_MANAGER:
                return new Manager(username, password, active);
            case ROLE_CLIENT:
                return new Client(username, password, active);
            default:
                throw new IllegalArgumentException("Nieprawidłowa rola: " + role);
        }
    }

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

    public abstract Role getRole();

    public abstract void setRole(Role role);

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", active=" + active + ", role=" + getRole() + "]";
    }
}
