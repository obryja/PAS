package org.example.rest;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;

@QuarkusTest
public class BaseTest {
    @Inject
    MongoDatabase mongoDatabase;

    @BeforeEach
    public void cleanup() {
        mongoDatabase.getCollection("books").drop();
        mongoDatabase.getCollection("users").drop();
        mongoDatabase.getCollection("rents").drop();
        mongoDatabase.getCollection("users").createIndex(new Document("username", 1), new IndexOptions().unique(true));
    }
}
