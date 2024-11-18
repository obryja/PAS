package org.example.rest.config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class MongoIndexCreator {

    @Autowired
    private MongoDatabase mongoDatabase;

    @EventListener(ApplicationReadyEvent.class)
    public void createIndexes() {
        mongoDatabase.getCollection("users").createIndex(new Document("username", 1), new IndexOptions().unique(true));
    }
}
