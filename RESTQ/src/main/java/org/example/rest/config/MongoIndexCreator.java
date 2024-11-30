package org.example.rest.config;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;


@ApplicationScoped
public class MongoIndexCreator {

    @Inject
    private MongoDatabase mongoDatabase;

    @PostConstruct
    public void createIndexes() {
        mongoDatabase.getCollection("users").createIndex(new Document("username", 1), new IndexOptions().unique(true));
    }
}
