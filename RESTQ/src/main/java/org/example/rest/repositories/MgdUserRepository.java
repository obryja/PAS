package org.example.rest.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.InsertOneResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.example.rest.models.User;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MgdUserRepository implements UserRepository {
    private final MongoCollection<User> userCollection;

    @Inject
    public MgdUserRepository(MongoDatabase mongoDatabase) {
        this.userCollection = mongoDatabase.getCollection("users", User.class);
    }

    @Override
    public User findById(String id) {
        return userCollection.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        userCollection.find().into(users);
        return users;
    }

    @Override
    public User create(User user) {
        InsertOneResult newUser = userCollection.insertOne(user);
        return userCollection.find(Filters.eq("_id", newUser.getInsertedId())).first();
    }

    @Override
    public User update(User user) {
        Bson filter = Filters.eq("_id", new ObjectId(user.getId()));
        ReplaceOptions options = new ReplaceOptions().upsert(false);
        userCollection.replaceOne(filter, user, options);
        return userCollection.find(filter).first();
    }

    @Override
    public User findByUsername(String username) {
        return userCollection.find(Filters.eq("username", username)).first();
    }

    @Override
    public List<User> findByUsernameContaining(String partialUsername) {
        List<User> users = new ArrayList<>();
        userCollection.find(Filters.regex("username", ".*" + partialUsername + ".*", "i")).into(users);
        return users;
    }
}
