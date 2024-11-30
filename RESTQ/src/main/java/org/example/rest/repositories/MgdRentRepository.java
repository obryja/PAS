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
import org.example.rest.models.Rent;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MgdRentRepository implements RentRepository {
    private final MongoCollection<Rent> rentCollection;

    @Inject
    public MgdRentRepository(MongoDatabase mongoDatabase) {
        this.rentCollection = mongoDatabase.getCollection("rents", Rent.class);
    }

    @Override
    public Rent findById(String id) {
        return rentCollection.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    @Override
        public List<Rent> findAll() {
        List<Rent> rents = new ArrayList<>();
        rentCollection.find().into(rents);
        return rents;
    }

    @Override
    public Rent create(Rent rent) {
        InsertOneResult newRent = rentCollection.insertOne(rent);
        return rentCollection.find(Filters.eq("_id", newRent.getInsertedId())).first();
    }

    @Override
    public Rent update(Rent rent) {
        Bson filter = Filters.eq("_id", new ObjectId(rent.getId()));
        ReplaceOptions options = new ReplaceOptions().upsert(false);
        rentCollection.replaceOne(filter, rent, options);
        return rentCollection.find(filter).first();
    }

    @Override
    public void delete(String id) {
        rentCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    @Override
    public List<Rent> findByUserIdAndEndDateIsNotNull(String userId) {
        Bson filter = Filters.and(
                Filters.eq("userId", new ObjectId(userId)),
                Filters.ne("endDate", null)
        );
        List<Rent> rents = new ArrayList<>();
        rentCollection.find(filter).into(rents);
        return rents;
    }

    @Override
    public List<Rent> findByUserIdAndEndDateIsNull(String userId) {
        Bson filter = Filters.and(
                Filters.eq("userId", new ObjectId(userId)),
                Filters.eq("endDate", null)
        );
        List<Rent> rents = new ArrayList<>();
        rentCollection.find(filter).into(rents);
        return rents;
    }

    @Override
    public List<Rent> findByBookIdAndEndDateIsNotNull(String bookId) {
        Bson filter = Filters.and(
                Filters.eq("bookId", new ObjectId(bookId)),
                Filters.ne("endDate", null)
        );
        List<Rent> rents = new ArrayList<>();
        rentCollection.find(filter).into(rents);
        return rents;
    }

    @Override
    public List<Rent> findByBookIdAndEndDateIsNull(String bookId) {
        Bson filter = Filters.and(
                Filters.eq("bookId", new ObjectId(bookId)),
                Filters.eq("endDate", null)
        );
        List<Rent> rents = new ArrayList<>();
        rentCollection.find(filter).into(rents);
        return rents;
    }
}
