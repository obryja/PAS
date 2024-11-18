package org.example.rest.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.rest.enums.Role;

@BsonDiscriminator(key = "_clazz", value = "manager")
public class Manager extends User {
    @BsonCreator
    public Manager(@BsonProperty("username") String username, @BsonProperty("password") String password, @BsonProperty("active") boolean active) {
        super(username, password, active, Role.ROLE_MANAGER);
    }

    public Manager() {}
}
