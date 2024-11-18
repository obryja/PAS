package org.example.rest.models;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.example.rest.enums.Role;

@BsonDiscriminator(key = "_clazz", value = "admin")
public class Admin extends User {
    @BsonProperty("role")
    private Role role;

    @BsonCreator
    public Admin(@BsonProperty("username") String username, @BsonProperty("password") String password, @BsonProperty("active") boolean active) {
        super(username, password, active);
        this.role = Role.ROLE_ADMIN;
    }

    public Admin() {}

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }
}
