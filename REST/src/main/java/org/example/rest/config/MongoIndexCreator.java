package org.example.rest.config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MongoIndexCreator {

    private final MongoDatabase mongoDatabase;

    @Autowired
    public MongoIndexCreator(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @PostConstruct
    public void createIndexes() {
        MongoCollection<Document> userCollection = mongoDatabase.getCollection("users");
        userCollection.createIndex(new Document("username", 1), new IndexOptions().unique(true));
    }
}
