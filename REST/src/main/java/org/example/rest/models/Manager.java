package org.example.rest.models;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.example.rest.enums.Role;

@BsonDiscriminator(key = "_clazz", value = "manager")
public class Manager extends User {
    @BsonProperty("role")
    private Role role;

    @BsonCreator
    public Manager(@BsonProperty("username") String username, @BsonProperty("password") String password, @BsonProperty("active") boolean active) {
        super(username, password, active);
        role = Role.ROLE_MANAGER;
    }

    public Manager() {}

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }
}
