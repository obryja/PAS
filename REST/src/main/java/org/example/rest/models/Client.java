package org.example.rest.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.rest.enums.Role;

@BsonDiscriminator(key = "_clazz", value = "client")
public class Client extends User {
    @BsonCreator
    public Client(@BsonProperty("username") String username, @BsonProperty("password") String password, @BsonProperty("active") boolean active) {
        super(username, password, active, Role.ROLE_CLIENT);
    }

    public Client() {}
}
