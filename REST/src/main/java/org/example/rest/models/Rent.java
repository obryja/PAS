package org.example.rest.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rents")
public class Rent {
    @Id
    private String id;

    public String getId() {
        return id;
    }
}
